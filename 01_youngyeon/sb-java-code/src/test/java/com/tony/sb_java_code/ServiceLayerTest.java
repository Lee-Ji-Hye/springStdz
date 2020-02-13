package com.tony.sb_java_code;

import com.tony.sb_java_code.dto.HelloDto;
import com.tony.sb_java_code.repository.HelloRepository;
import com.tony.sb_java_code.service.HelloService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

// 참고: https://nesoy.github.io/articles/2018-09/Mockito
@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("default")
public class ServiceLayerTest {

    @InjectMocks // @Mock이나 @Spy 객체를 자신의 멤버 클래스와 일치하면 주입시킵니다.
    HelloService helloService;

    @Mock // mock 객체를 만들어서 반환합니다.
    HelloRepository helloRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllHelloTest()
    {
        List<HelloDto> list = new ArrayList<HelloDto>();
        HelloDto helloDto = new HelloDto(1, "tony", "spark", "tony@naver.com");
        HelloDto helloDto2 = new HelloDto(2, "kim", "suzi", "suzi@naver.com");
        HelloDto helloDto3 = new HelloDto(3, "park", "chanho", "chanho@naver.com");

        list.add(helloDto);
        list.add(helloDto2);
        list.add(helloDto3);

        // 매개변수 검증
        // 모든 HelloDto 타입 매개변수를 받을 경우 "list"를 돌려줍니다.
        when(helloRepository.findAll()).thenReturn(list);
        // test
        List<HelloDto> helloList = helloService.findAll();
        assertEquals(3, helloList.size());
        // 횟수 검증
        verify(helloRepository, times(1)).findAll();
        // 시간에 대한 검증
        verify(helloRepository, timeout(100)).findAll();
        // 시간 & 횟수 검증
        verify(helloRepository, timeout(100).times(1)).findAll();

        // 순서에 대한 검증
        List singleMock = mock(List.class);
        singleMock.add(helloDto);
        singleMock.add(helloDto2);
        singleMock.add(helloDto3);

        // 구체적인 클래스를 mock 처리합니다.
        LinkedList mockedList = mock(LinkedList.class);
        // stubbing
        // 소프트웨어 개발에서 메소드 스텁(method stub) 혹은 간단히 스텁은 다른 프로그래밍 기능을 대리하는 코드이다.
        // https://ko.wikipedia.org/wiki/%EB%A9%94%EC%86%8C%EB%93%9C_%EC%8A%A4%ED%85%81
        when(mockedList.get(0)).thenReturn("first"); // get(0)이 호출되면 "first"를 반환합니다.
        when(mockedList.get(1)).thenThrow(new RuntimeException()); // get(1)이 호출되면 RuntimeException 에러를 발생합니다.
        System.out.println(mockedList.get(0)); // "first"
//        System.out.println(mockedList.get(1)); // RuntimeException()

        // 중위 순회 Case
        InOrder inOrder = inOrder(singleMock);
        // 순서에 대한 검증
        inOrder.verify(singleMock).add(helloDto);
        inOrder.verify(singleMock).add(helloDto2);
        inOrder.verify(singleMock).add(helloDto3);

        // 아무일이 일어나지 않는 mock에 대한 검증
        List mockOne = mock(List.class);
        // mockOne만 add를 호출합니다.
        mockOne.add("one");
        // 일반적인 검증입니다.
        verify(mockOne).add("one");
        verify(mockOne, never()).add("two");

        // 연속적인 Stubbing
        when(helloRepository.findAll())
                .thenThrow(new RuntimeException()) // 첫번째 return
                .thenReturn(list);

//        // 첫번째 call
//        helloRepository.findAll();
//        // 두번째 call
//        helloRepository.findAll();
//        // 마지막 call
//        helloRepository.findAll();

        // Spy를 통해 실제 객체를 생성하고 필요한 부분에만 mock처리하여 검증을 진행할 수 있습니다.
        List spy = spy(list);
        // Method에 대한 stubbing
        when(spy.size()).thenReturn(100);
        // 실제 객체의 Method를 호출합니다.
        spy.add("one");
        spy.add("two");
        // "one"
        System.out.println(spy.get(0));
        // Stubbing된 Method의 결과 : 100
        System.out.println(spy.size());
    }


}
