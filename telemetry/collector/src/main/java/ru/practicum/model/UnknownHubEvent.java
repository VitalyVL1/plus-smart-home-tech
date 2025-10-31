package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;

/**
 * Резервный класс для обработки неизвестных или неподдерживаемых типов событий хаба.
 * Используется по умолчанию при получении событий хаба с неизвестным типом.
 * Сохраняет оригинальный тип для последующего анализа и отладки.
 *
 * @see HubEvent
 * @see HubEventType#UNKNOWN
 */
@Getter
@Setter
@ToString(callSuper = true)
public class UnknownHubEvent extends HubEvent {

    /**
     * Тип события, фиксированное значение UNKNOWN.
     * Указывает на то, что тип события хаба не распознан системой.
     */
    @NotNull
    private final HubEventType type = HubEventType.UNKNOWN;

    /**
     * Оригинальное значение типа события хаба, полученное из JSON.
     * Сохраняется для анализа неподдерживаемых типов событий и отладки.
     */
    @JsonIgnore
    private String originalType;

    /**
     * Конструктор для десериализации событий хаба с неизвестным типом.
     *
     * @param originalType оригинальное значение типа из JSON-запроса.
     */
    @JsonCreator
    public UnknownHubEvent(@JsonProperty("type") String originalType) {
        this.originalType = originalType;
    }
}
