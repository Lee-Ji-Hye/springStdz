package com.tony.sb_java_code.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Profile(value = "dev")
public class WebClientRunner implements ApplicationRunner {

   @Autowired
   WebClient webClient;

   @Override
   public void run(ApplicationArguments args) throws Exception {
      Flux<String> delay3 = webClient.get().uri("http://localhost:8080/delay?sec=3")
         .retrieve()
         .bodyToFlux(String.class);

      delay3.subscribe(i -> System.out.println(System.currentTimeMillis()));

      Flux<String> delay2 = webClient.get().uri("http://localhost:8080/delay?sec=2")
         .retrieve()
         .bodyToFlux(String.class);

      delay2.subscribe(i -> System.out.println(System.currentTimeMillis()));
   }
}
