package com.scada.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://scada.pratikshat.com", "http://198.7.114.147:8888", "http://localhost:8888", "http://localhost:4200")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
