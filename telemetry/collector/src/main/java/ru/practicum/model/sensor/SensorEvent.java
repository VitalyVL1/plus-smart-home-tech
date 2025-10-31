package ru.practicum.model.sensor;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * Абстрактный базовый класс для всех событий, генерируемых датчиками умного дома.
 * Определяет общую структуру событий и механизм полиморфной десериализации.
 * Используется универсальным контроллером для обработки различных типов событий датчиков.
 *
 * <p>Особенности архитектуры:
 * <ul>
 *   <li>Определяет общие метаданные для всех событий датчиков</li>
 *   <li>Обеспечивает единообразную обработку различных типов датчиков</li>
 *   <li>Гарантирует корректную десериализацию на основе поля 'type'</li>
 * </ul>
 *
 * @see JsonTypeInfo
 * @see JsonSubTypes
 * @see UnknownSensorEvent
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = UnknownSensorEvent.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MotionSensorEvent.class, name = "MOTION_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = TemperatureSensorEvent.class, name = "TEMPERATURE_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = LightSensorEvent.class, name = "LIGHT_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = ClimateSensorEvent.class, name = "CLIMATE_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = SwitchSensorEvent.class, name = "SWITCH_SENSOR_EVENT")
})

@Getter
@Setter
@ToString
@Schema(description = "Базовый класс для реализации событий датчиков.")
public abstract class SensorEvent {

    /**
     * Уникальный идентификатор события датчика.
     * Используется для отслеживания и корреляции событий в системе.
     */
    @NotBlank
    @Schema(description = "Идентификатор события датчика.")
    private String id;

    /**
     * Уникальный идентификатор хаба, к которому подключен датчик.
     * Используется как ключ для партиционирования в Kafka - гарантирует порядок событий от одного хаба.
     */
    @NotBlank
    @Schema(description = "Идентификатор хаба, связанного с событием.")
    private String hubId;

    /**
     * Временная метка создания события датчика.
     * Используется для временного анализа данных и построения временных рядов.
     * По умолчанию устанавливается текущее время в момент создания объекта.
     */
    @NotNull
    @Schema(description = "Временная метка события. По умолчанию устанавливается текущее время.")
    private Instant timestamp = Instant.now();

    /**
     * Тип события датчика, определяющий конкретную реализацию и формат полезной нагрузки.
     * Используется системой для:
     * <ul>
     *   <li>Маршрутизации события к соответствующему обработчику</li>
     *   <li>Определения Avro схемы для сериализации</li>
     *   <li>Полиморфной десериализации</li>
     * </ul>
     *
     * @return SensorEventType не должен быть null
     */
    @NotNull
    @Schema(description = "Перечисление типов событий датчиков. " +
                          "Определяет различные типы событий, которые могут быть связаны с датчиками.")
    public abstract SensorEventType getType();
}
