package com.tony.sb_java_code.broker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.jms.Session;

import static com.tony.sb_java_code.broker.ActiveMQConfig.ORDER_QUEUE;

@Component
@Slf4j
public class OrderConsumer {

   @Autowired
   WebClient webClient;

   @JmsListener(destination = ORDER_QUEUE)
   public void receiveMessage(@Payload Order order,
                              @Headers MessageHeaders headers,
                              Message message, Session session) {

      log.info("received <" + order + ">");
      log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
      log.info("######          Message Details           #####");
      log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
      log.info("headers: " + headers);
      log.info("message: " + message);
      log.info("session: " + session);
      log.info("- - - - - - - - - - - - - - - - - - - - - - - -");

      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("sec", "3");
      Flux<String> delay3 = webClient.get().uri(uriBuilder ->
         uriBuilder.path("/delay")
            .queryParams(params).build())
            .retrieve()
            .bodyToFlux(String.class);
//      delay3.subscribe(i-> log.info(i));

//      if (order.getContent().contains("3")) {
//         throw new RuntimeException("처리할 수 없습니다.");
//      }
   }

}