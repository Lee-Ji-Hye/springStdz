package com.tony.sb_java_code.controller;

import com.tony.sb_java_code.broker.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class DelayAPI {

    @GetMapping("/delay")
    public String delay(int sec, @ModelAttribute Order order) throws InterruptedException {
        log.info("delay receive data: " + sec);
        log.info(order.toString());
        TimeUnit.SECONDS.sleep(sec);
        return "current delay page: + " + sec + " " + order.toString();
    }

}
