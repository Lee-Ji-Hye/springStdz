package com.tony.sb_java_code.config;

//@Configuration
//public class HelloFluxConfig implements WebFluxConfigurer {
//
//    @Autowired
//    ApplicationContext applicationContext;
//
//    private int MAX_AGE_SECONDS = 3600;
//
//    /**
//     * core에서 설정함.
//     * @return
//     */
//    @Bean
//    public WebFluxConfigurer corsConfigurer() {
//        return new WebFluxConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedMethods("GET", "POST", "PUT", "DELETE")
//                        .allowedOrigins("*")
//                        .allowedHeaders("*")
//                        .allowCredentials(true)
//                        .maxAge(MAX_AGE_SECONDS);
//            }
//        };
//    }
//
//    @Bean
//    public LayoutDialect layoutDialect() {
//        return new LayoutDialect();
//    }
//
//    @Bean
//    public ISpringWebFluxTemplateEngine thymeleafTemplateEngine() {
//        SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();
//        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
//        templateEngine.setEnableSpringELCompiler(true);			// Spring EL 사용
//        templateEngine.addDialect(layoutDialect());
//        templateEngine.addDialect(new JSONMapperDialect());
//        return templateEngine;
//    }
//
//    @Bean
//    public ITemplateResolver thymeleafTemplateResolver() {
//        final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//        resolver.setApplicationContext(this.applicationContext);
//        resolver.setPrefix("classpath:templates/");
//        resolver.setSuffix(".html");
//        resolver.setTemplateMode(TemplateMode.HTML);
//        resolver.setCacheable(false);
//        resolver.setCheckExistence(false);
//        return resolver;
//    }
//
//    @Bean
//    public ThymeleafReactiveViewResolver thymeleafReactiveViewResolver() {
//        ThymeleafReactiveViewResolver viewResolver = new ThymeleafReactiveViewResolver();
//        viewResolver.setTemplateEngine(thymeleafTemplateEngine());
//        return viewResolver;
//    }
//
//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        registry.viewResolver(thymeleafReactiveViewResolver());
//    }
//
//}
