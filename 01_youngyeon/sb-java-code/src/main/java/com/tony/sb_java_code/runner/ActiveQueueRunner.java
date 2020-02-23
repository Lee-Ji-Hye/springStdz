package com.tony.sb_java_code.runner;

import com.tony.sb_java_code.broker.Order;
import com.tony.sb_java_code.broker.OrderSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Profile(value = "dev")
@Slf4j
@EnableScheduling
public class ActiveQueueRunner implements ApplicationRunner {

   @Autowired
   private OrderSender orderSender;

   //    @Scheduled(fixedRateString = "3000", initialDelay = 1000)
   public void activeSend() throws InterruptedException {
      log.info("Spring Boot Embedded ActiveMQ Configuration Example");

      for (int i = 0; i < 5; i++){
         Order myMessage = new Order(i + " - Sending JMS Message using Embedded activeMQ", new Date());
         orderSender.send(myMessage);
      }

      log.info("Waiting for all ActiveMQ JMS Messages to be consumed");
      TimeUnit.SECONDS.sleep(3);
   }

   @Override
   public void run(ApplicationArguments args) throws Exception {
      activeSend();
   }
}
