package ru.practicum.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public abstract class BaseErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());

        // Логируем полученную ошибку
        log.warn("Feign error for method: {}, status: {}", methodKey, status);

        // Обрабатываем 404 ошибку - возвращаем специальное исключение
        if (status == HttpStatus.NOT_FOUND) {
            return new ResourceNotFoundException("Resource not found: " + methodKey);
        }

        // Для всех остальных ошибок сервера (5xx) бросаем ServiceUnavailableException
        if (status.is5xxServerError()) {
            return new ServiceTemporaryUnavailableException("Service temporary unavailable: " + methodKey);
        }

        // Для других 4xx ошибок
        if (status.is4xxClientError()) {
            return new BadRequestException("Service error: " + methodKey + ", stutus: " + status);
        }

        // Для всех остальных ошибок используем стандартную обработку
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
