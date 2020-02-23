package com.tony.sb_java_code.broker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import static com.tony.sb_java_code.broker.ActiveMQConfig.ORDER_QUEUE;

@Service
@Slf4j
public class OrderSender {

   @Autowired
   private JmsTemplate jmsTemplate;

   public void send(Order myMessage) {
      log.info("sending with convertAndSend() to queue <" + myMessage + ">");
      jmsTemplate.convertAndSend(ORDER_QUEUE, myMessage);
   }
}
