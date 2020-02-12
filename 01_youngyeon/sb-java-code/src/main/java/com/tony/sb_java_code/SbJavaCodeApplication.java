package com.tony.sb_java_code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class SbJavaCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbJavaCodeApplication.class, args);
    }

}
