package com.tony.sb_java_code.repository;

import org.springframework.stereotype.Repository;

@Repository
public class MysqlMessageRepository implements MessageRepository {
    @Override
    public void save() {
        System.out.println("saving in mysql");
    }
}
