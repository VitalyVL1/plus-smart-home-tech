package ru.practicum.security;

import lombok.Data;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class BaseSecurityUsersProperties {
    private Map<String, UserConfig> users = new HashMap<>();

    @Data
    public static class UserConfig {
        private String password;
        private String[] roles;
    }

    public List<UserDetails> toUserDetailsList() {
        return users.entrySet().stream()
                .map(entry -> User.withUsername(entry.getKey())
                        .password("{noop}" + entry.getValue().getPassword())
                        .roles(entry.getValue().getRoles())
                        .build())
                .toList();
    }
}
