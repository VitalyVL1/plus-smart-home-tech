package ru.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.annotation.LogAllMethodsAspect;

@Configuration
public class LoggingConfig {
    @Bean
    public LogAllMethodsAspect loggingAspect() {
        return new LogAllMethodsAspect();
    }
}
