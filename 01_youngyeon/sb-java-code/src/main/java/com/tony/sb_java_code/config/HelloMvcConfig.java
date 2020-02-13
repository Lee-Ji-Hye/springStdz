package com.tony.sb_java_code.config;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

public class HelloMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

//    @Override
//    public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
//        viewControllerRegistry.addViewController("/").setViewName("/html/main/index.html");
//    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8"); // <- this was added
        resolver.setForceContentType(true); // <- this was added
        resolver.setContentType("text/html; charset=UTF-8"); // <- this was added
        registry.viewResolver(resolver);
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);			// Spring EL 사용
        templateEngine.addDialect(layoutDialect());
        templateEngine.addDialect(new JSONMapperDialect());
        return templateEngine;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setCharacterEncoding("UTF-8"); // forcing UTF-8
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("classpath:/templates/");	// HTML 파일 위치
        templateResolver.setSuffix(".html");					// HTML 확장명 사용
        templateResolver.setTemplateMode(TemplateMode.HTML);	// HTML5 값은 비권장 됨
        templateResolver.setCacheable(false);					// 캐시 사용 안함
        return templateResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")			// 리소스와 매칭될 url
                .addResourceLocations("classpath:/static/");		// 리소스 위치
        // .setCachePeriod(60);							// 캐시 지속 시간
        //.setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic());

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/");
    }
}
