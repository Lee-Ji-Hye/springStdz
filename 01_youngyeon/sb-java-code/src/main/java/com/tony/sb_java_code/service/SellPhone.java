package com.tony.sb_java_code.service;

import com.tony.sb_java_code.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SellPhone {
    @Autowired
    @Qualifier(value = "mysqlMessageRepository")
    private MessageRepository mysqlMessageRepository;

    @Autowired
    @Qualifier(value = "inMemoryMessageRepository")
    private MessageRepository inMemoryMessageRepository;

    public void sendMessage(){
        mysqlMessageRepository.save();
        inMemoryMessageRepository.save();
    }
}
