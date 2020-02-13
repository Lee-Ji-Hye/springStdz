package com.tony.sb_java_code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelloDto {
    @Id
    @GeneratedValue
    private int id;
    private String fistName;
    private String lastName;
    private String email;
}
