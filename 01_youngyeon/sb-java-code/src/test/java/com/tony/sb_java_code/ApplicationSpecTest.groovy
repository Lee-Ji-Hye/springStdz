package com.tony.sb_java_code

import com.tony.sb_java_code.dto.HelloDto
import com.tony.sb_java_code.repository.HelloRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.hamcrest.Matchers.is
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

// https://github.com/chrismacp/example-springboot-spock
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@SpringBootTest
@AutoConfigureMockMvc
class ApplicationSpecTest extends Specification {

//    @Autowired
//    ApplicationContext context

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private HelloRepository helloRepository

    def "should return helloRepository HelloDto details as JSON"() {

        given:
        HelloDto helloDto = new HelloDto()
        helloDto.fistName = "tony"
        helloDto.lastName = "spark"
        helloDto.id = 1
        helloDto.email = "tony@gmail.com"
        helloRepository.save(helloDto)

        when:
        def response = mockMvc.perform(get("/api/list").contentType(APPLICATION_JSON))

        then:
        response
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath('$.msg', is('null')))
    }

//    def "test context loads"() {
//        expect:
//        context != null
////		context.containsBean("helloWorldService")
////		context.containsBean("simpleBootApp")
////		context.containsBean("scopedHelloWorldService")
//    }
}
