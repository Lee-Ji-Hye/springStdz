package com.tony.sb_java_code.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DelayAPI {

    @GetMapping("/delay")
    public String delay(int sec) {
        System.out.println("delay receive data: " + sec);
        try {
            Thread.sleep(sec * 1000);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return "current delay page: + " + sec;
    }

}
