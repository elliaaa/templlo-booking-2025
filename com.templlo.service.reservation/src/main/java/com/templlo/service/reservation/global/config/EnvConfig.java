package com.templlo.service.reservation.global.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    @PostConstruct
    public void loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./com.templlo.service.reservation/")
                .load();
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
    }
}
