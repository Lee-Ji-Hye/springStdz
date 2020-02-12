package com.tony.sb_java_code.dto;

import lombok.Data;

@Data
public class ResultDTO {
    private String msg;
    private String code;
    private Object body;
}
