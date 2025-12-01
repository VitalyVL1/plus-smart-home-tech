package ru.practicum.dto.exception;

import lombok.Builder;

import java.util.List;

public record ErrorResponse(Throwable cause,
                            List<StackTraceElement> stackTrace,
                            String httpStatus,
                            String userMessage,
                            String message,
                            List<Throwable> suppressed,
                            String localizedMessage) {
    @Builder
    public ErrorResponse {
    }
}
