package com.pjt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

	/**
	 * 기본 경로에 대한 view 설정
	 * */
	@Override
	public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
		viewControllerRegistry.addViewController("/").setViewName("index");
	}

}
