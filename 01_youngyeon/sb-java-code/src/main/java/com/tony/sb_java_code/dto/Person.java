package com.tony.sb_java_code.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@ToString
public class Person {
    @Autowired
    private Job job;
}
