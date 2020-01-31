package com.example.demo;

import org.springframework.beans.factory.annotation.Configurable;
        import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.http.converter.HttpMessageConverter;

// @Configuration(proxyBeanMethods = false)
public class MyConfiguration {
    // @Bean
    public HttpMessageConverters customConverters() {
        HttpMessageConverter<?> add = null;
        HttpMessageConverter<?> ano = null;
        return new HttpMessageConverters(add, ano);
    }
}
