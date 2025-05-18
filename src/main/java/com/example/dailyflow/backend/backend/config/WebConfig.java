package com.example.dailyflow.backend.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")                            // todas las rutas
                .allowedOrigins("http://localhost:5173")      // tu front
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
