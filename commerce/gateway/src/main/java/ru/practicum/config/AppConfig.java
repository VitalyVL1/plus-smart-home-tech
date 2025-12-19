package ru.practicum.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;

@ConfigurationProperties("gateway")
@Getter
@Setter
@ToString
public class AppConfig {

    private String username;
    private String password;
    private String roles;

    @Bean
    MapReactiveUserDetailsService userDetailsService() {
        var user = User.withUsername(username)
                .password("{noop}" + password)
                .roles(roles)
                .build();

        return new MapReactiveUserDetailsService(user);
    }
}
