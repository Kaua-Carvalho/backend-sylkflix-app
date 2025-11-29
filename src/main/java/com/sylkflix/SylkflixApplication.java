package com.sylkflix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SylkflixApplication {

    public static void main(String[] args) {
        SpringApplication.run(SylkflixApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("🎬 SylkFlix API está rodando!");
        System.out.println("📚 Swagger UI: https://backend-sylkflix-app.onrender.com/swagger-ui.html");
        System.out.println("📖 API Docs: https://backend-sylkflix-app.onrender.com/api-docs");
        System.out.println("===========================================\n");
    }
}