package ru.practicum.model.hub;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.UnknownHubEvent;
import ru.practicum.model.hub.device.DeviceAddedEvent;
import ru.practicum.model.hub.device.DeviceRemovedEvent;
import ru.practicum.model.hub.scenario.ScenarioAddedEvent;
import ru.practicum.model.hub.scenario.ScenarioRemovedEvent;

import java.time.Instant;

/**
 * Абстрактный базовый класс для всех событий, генерируемых хабами умного дома.
 * Определяет общую структуру событий и механизм полиморфной десериализации.
 * Используется универсальным контроллером для обработки различных типов событий хаба.
 *
 * <p>Механизм наследования и полиморфизма:
 * <ul>
 *   <li>Определяет общие поля для всех событий (hubId, timestamp)</li>
 *   <li>Позволяет единообразно обрабатывать разные типы событий</li>
 *   <li>Обеспечивает корректную десериализацию на основе поля 'type'</li>
 * </ul>
 *
 * @see JsonTypeInfo
 * @see JsonSubTypes
 * @see UnknownHubEvent
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = UnknownHubEvent.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedEvent.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = "DEVICE_REMOVED"),
        @JsonSubTypes.Type(value = ScenarioAddedEvent.class, name = "SCENARIO_ADDED"),
        @JsonSubTypes.Type(value = ScenarioRemovedEvent.class, name = "SCENARIO_REMOVED"),
})

@Getter
@Setter
@ToString
@Schema(description = "Базовый класс для реализации событий хаба.")
public abstract class HubEvent {

    /**
     * Уникальный идентификатор хаба-отправителя события.
     * Используется как ключ для партиционирования в Kafka - гарантирует порядок событий от одного хаба.
     * Все события от одного хаба попадают в одну партицию Kafka.
     */
    @NotBlank
    @Schema(description = "Идентификатор хаба, связанный с событием.")
    private String hubId;

    /**
     * Временная метка создания события в системе.
     * Используется для временного упорядочивания событий и анализа временных рядов.
     * По умолчанию устанавливается текущее время в момент создания объекта.
     */
    @NotNull
    @Schema(description = "Временная метка события. По умолчанию устанавливается текущее время.")
    private Instant timestamp = Instant.now();

    /**
     * Тип события хаба, определяющий конкретную реализацию и формат данных.
     * Используется системой для:
     * <ul>
     *   <li>Маршрутизации события к соответствующему обработчику</li>
     *   <li>Определения Avro схемы для сериализации</li>
     *   <li>Полиморфной десериализации JSON</li>
     * </ul>
     *
     * @return HubEventType не должен быть null
     */
    @NotNull
    @Schema(description = "Перечисление типов событий хаба.")
    public abstract HubEventType getType();
}
