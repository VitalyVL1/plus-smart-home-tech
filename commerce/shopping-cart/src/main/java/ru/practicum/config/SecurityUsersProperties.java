package ru.practicum.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import ru.practicum.security.BaseSecurityUsersProperties;

@Component
@ConfigurationProperties(prefix = "app.security")
@RefreshScope
public class SecurityUsersProperties extends BaseSecurityUsersProperties {

}
