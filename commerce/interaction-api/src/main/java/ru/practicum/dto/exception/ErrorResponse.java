package ru.practicum.dto.exception;

import lombok.Builder;

import java.util.List;

public record ErrorResponse(
        String message,           // общее сообщение
        List<Issue> issues        // конкретные проблемы
) {
    @Builder
    public ErrorResponse {
    }

    public record Issue(
            String location,      // место ошибки
            String description    // описание ошибки
    ) {
        @Builder
        public Issue {
        }
    }
}