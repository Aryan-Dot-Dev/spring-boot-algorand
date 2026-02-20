package dev.suspicious.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/vcf/**")
                .allowedOrigins(
                        "http://localhost:8080",
                        "http://localhost:8081",
                        "https://spring-boot-algorand.onrender.com",
                        "https://gene-harmony.onrender.com/",
                        "http://10.16.202.37:8081",
                        "192.168.137.1:8081",
                        "http://10.16.202.37:8080",
                        "192.168.137.1:8080"
                )
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
