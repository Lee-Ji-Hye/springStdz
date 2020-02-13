package com.tony.sb_java_code.repository;

import com.tony.sb_java_code.dto.HelloDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelloRepository extends JpaRepository<HelloDto, Integer> {
}
