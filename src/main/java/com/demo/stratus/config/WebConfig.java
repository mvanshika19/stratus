package com.demo.stratus.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // This automatically sends anyone who visits localhost:8080 to Swagger
        registry.addRedirectViewController("/", "/swagger-ui/index.html");
    }
}