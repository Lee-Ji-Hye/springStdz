package com.tony.sb_java_code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class SbJavaCodeApplication implements ApplicationRunner {

    static <T extends Comparable<T>> long countGreaterrThan(T[] arr, T elem) {
        return Arrays.stream(arr).filter(s -> s.compareTo(elem) > 0).count();
    }

    public static void main(String[] args) {
        SpringApplication.run(SbJavaCodeApplication.class, args);
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("hello study project");


    }
}
