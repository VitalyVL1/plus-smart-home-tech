package ru.practicum.repository;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRepository {
    private final Map<String, SensorsSnapshotAvro> snapshots = new ConcurrentHashMap<>();

    public Optional<SensorsSnapshotAvro> get(String hubId) {
        return Optional.ofNullable(snapshots.get(hubId));
    }

    public SensorsSnapshotAvro put(SensorsSnapshotAvro sensorsSnapshotAvro) {
        snapshots.put(sensorsSnapshotAvro.getHubId(), sensorsSnapshotAvro);
        return sensorsSnapshotAvro;
    }
}
