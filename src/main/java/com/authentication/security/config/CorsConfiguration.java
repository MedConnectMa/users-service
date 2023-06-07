package com.authentication.security.config;


import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    @Bean
    public WebMvcConfigurer corsConfig() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "PUT", "POST", "DELETE")
                        .allowedOrigins("http://localhost:3000", "http://localhost:19006")
                        .allowedHeaders("Authorization", "Content-Type")
                        .exposedHeaders("Authorization");
            }
        };
    }
}

