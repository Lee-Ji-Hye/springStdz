package com.tony.sb_java_code.repository;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryMessageRepository implements MessageRepository {

    @Override
    public void save() {
        System.out.println("saving in memory");
    }
}
