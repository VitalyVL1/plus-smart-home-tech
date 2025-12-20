package ru.practicum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import ru.practicum.security.BaseSecurityConfig;
import ru.practicum.security.BaseSecurityUsersProperties;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends BaseSecurityConfig {

    public SecurityConfig(BaseSecurityUsersProperties usersProperties) {
        super(usersProperties);
    }
}
