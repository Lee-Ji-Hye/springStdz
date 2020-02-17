package com.tony.sb_java_code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardVo {
    private Long id;
    private String title;
    private String content;

    private LocalDateTime regDateTime;
    private String regWriter;
}
