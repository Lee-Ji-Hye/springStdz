package com.tony.sb_java_code.service;

import com.tony.sb_java_code.dto.HelloDto;
import com.tony.sb_java_code.repository.HelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HelloService {

    @Autowired
    HelloRepository helloRepository;

    public List<HelloDto> findAll() {
        return helloRepository.findAll();
    }
}
