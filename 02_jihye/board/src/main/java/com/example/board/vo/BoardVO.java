package com.example.board.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder 
@AllArgsConstructor 
@NoArgsConstructor
public class BoardVO {
    private String userId; 
    private String title;
    private String content;

    private LocalDateTime regDate; 
    private LocalDateTime modiDate;
    private char isDelete;
    
}
