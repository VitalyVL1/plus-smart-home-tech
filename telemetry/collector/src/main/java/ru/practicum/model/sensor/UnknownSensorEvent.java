package ru.practicum.model.sensor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Резервный класс для обработки неизвестных или неподдерживаемых типов событий датчиков.
 * Используется по умолчанию при получении событий с неизвестным типом.
 * Сохраняет оригинальный тип для последующего анализа и отладки.
 *
 * @see SensorEvent
 * @see SensorEventType#UNKNOWN
 */
@Getter
@Setter
@ToString(callSuper = true)
public class UnknownSensorEvent extends SensorEvent {

    /**
     * Тип события, фиксированное значение UNKNOWN.
     * Указывает на то, что тип события не распознан системой.
     */
    @NotNull
    private final SensorEventType type = SensorEventType.UNKNOWN;

    /**
     * Оригинальное значение типа события, полученное из JSON.
     * Сохраняется для анализа неподдерживаемых типов событий и отладки.
     */
    @JsonIgnore
    private String originalType; // сохраняем оригинальный тип из JSON

    /**
     * Конструктор для десериализации событий с неизвестным типом.
     *
     * @param originalType оригинальное значение типа из JSON-запроса.
     */
    @JsonCreator
    public UnknownSensorEvent(@JsonProperty("type") String originalType) {
        this.originalType = originalType;
    }
}
