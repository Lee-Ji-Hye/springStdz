package com.tony.sb_java_code;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootTest
public class WebClientTest {

    @Autowired
    WebClient webClient;

    @Test
    public void webClientTest() {

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
