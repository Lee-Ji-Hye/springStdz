package com.tony.sb_java_code.config;

import com.tony.sb_java_code.dto.Job;
import com.tony.sb_java_code.dto.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SpringCoreConfig {
    @Bean
    public Person person(){
        return new Person();
    }
    @Bean
    @Primary
    public Job javaDeveloper(){
        return new Job("Java Developer");
    }
    @Bean
    public Job frontEndDeveloper(){
        return new Job("Front end Developer");
    }
}
