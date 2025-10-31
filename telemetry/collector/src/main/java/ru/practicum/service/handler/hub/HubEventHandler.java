package ru.practicum.service.handler.hub;

import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;

/**
 * Интерфейс для обработчиков событий хаба.
 * Определяет контракт для классов, обрабатывающих различные типы событий, происходящих в хабе.
 * Каждая реализация должна обрабатывать конкретный тип события {@link HubEventType}.
 *
 * @see HubEvent
 * @see HubEventType
 */
public interface HubEventHandler {

    /**
     * Возвращает тип события, который обрабатывает данный обработчик.
     * Используется для маршрутизации событий к соответствующим обработчикам.
     *
     * @return HubEventType, не должен быть null
     */
    HubEventType getMessageType();

    /**
     * Обрабатывает событие хаба.
     * Реализация должна содержать логику преобразования и обработки конкретного типа события.
     *
     * @param event событие для обработки, не должно быть null
     * @throws IllegalArgumentException если event равен null
     * @throws RuntimeException         если произошла ошибка при обработке события
     */
    void handle(HubEvent event);
}
