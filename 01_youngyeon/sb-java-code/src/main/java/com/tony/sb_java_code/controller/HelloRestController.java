package com.tony.sb_java_code.controller;

import com.tony.sb_java_code.dto.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloRestController {
    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return new ResponseEntity<ResultDto>(new ResultDto(), HttpStatus.OK);
    }


}
