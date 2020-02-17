package com.tony.sb_java_code.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    // 404는 어떻게 처리?

   @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeException(RuntimeException re) {
        log.error("RuntimeException: ", re);
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> runtimeException(Exception re) {
        log.error("Exception: ", re);
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> runtimeException(DataAccessException re) {
        log.error("DataAccessException: ", re);
        return ResponseEntity.badRequest().build();
    }
}
