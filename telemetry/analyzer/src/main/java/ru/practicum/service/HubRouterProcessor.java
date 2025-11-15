package ru.practicum.service;

import com.google.protobuf.Timestamp;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.practicum.dal.model.Action;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.HubRouterControllerBlockingStub;

import java.time.Instant;

import static ru.practicum.dal.model.mapper.ActionMapper.toDeviceActionProto;

/**
 * Сервис для отправки действий на хабы через gRPC.
 * Преобразует доменные действия в gRPC-сообщения и отправляет их в hub-router.
 */
@Service
@Slf4j
public class HubRouterProcessor {

    @GrpcClient("hub-router")
    private HubRouterControllerBlockingStub hubRouterClient;

    /**
     * Обрабатывает действие сценария и отправляет его на хаб через gRPC.
     *
     * @param hubId        идентификатор хаба
     * @param sensorId     идентификатор сенсора
     * @param scenarioName название сценария
     * @param action       действие для выполнения
     */
    public void handleAction(String hubId, String sensorId, String scenarioName, Action action) {
        if (hubRouterClient == null) {
            log.error("GRPC client is not initialized");
            return;
        }
        DeviceActionProto deviceActionProto = toDeviceActionProto(sensorId, action);

        if (deviceActionProto == null) {
            log.warn("Device action is null for hubId: {}, sensorId: {}", hubId, sensorId);
            return;
        }

        Instant now = Instant.now();
        DeviceActionRequest deviceActionRequest = DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(scenarioName)
                .setAction(toDeviceActionProto(sensorId, action))
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(now.getEpochSecond())
                        .setNanos(now.getNano())
                        .build())
                .build();

        try {
            hubRouterClient.handleDeviceAction(deviceActionRequest);
        } catch (StatusRuntimeException e) {
            switch (e.getStatus().getCode()) {
                case UNAVAILABLE -> log.warn("Server hub-router unavailable: {}", e.getMessage());
                case DEADLINE_EXCEEDED -> log.warn("Timeout call handleDeviceAction");
                case INVALID_ARGUMENT -> log.warn("Invalid argument: {}", e.getMessage());
                default -> log.warn("GRPC error, couldn't call handleDeviceAction: {}", e.getStatus(), e);
            }

        }

    }

}
