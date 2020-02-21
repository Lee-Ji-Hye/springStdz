package com.tony.sb_java_code;

import com.tony.sb_java_code.customQualifier.Shepperd;
import com.tony.sb_java_code.dto.Person;
import com.tony.sb_java_code.service.SellPhone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootApplication
@Slf4j
//@ComponentScan(basePackages = "com.tony.sb_java_code",
//        excludeFilters = {
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SellPhone.class),
//                @ComponentScan.Filter(Configuration.class)
//        })
public class SbJavaCodeApplication implements ApplicationRunner {

    @Autowired
    SellPhone sellPhone;

    @Autowired
    Person person;

    @Autowired
    Person frontEndDeveloper;

    @Autowired
    Shepperd shepperd;

    @Autowired
    WebClient webClient;



    public static void main(String[] args) {
        SpringApplication.run(SbJavaCodeApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        sellPhone.sendMessage();
        log.info(person.toString());
        log.info(frontEndDeveloper.toString());
        log.info(shepperd.toString());

        Flux<String> delay3 = webClient.get().uri("http://localhost:8080/delay?sec=3")
                .retrieve()
                .bodyToFlux(String.class);

        delay3.subscribe(i-> System.out.println(System.currentTimeMillis()));

        Flux<String> delay2 = webClient.get().uri("http://localhost:8080/delay?sec=2")
                .retrieve()
                .bodyToFlux(String.class);

        delay2.subscribe(i-> System.out.println(System.currentTimeMillis()));
    }
}
