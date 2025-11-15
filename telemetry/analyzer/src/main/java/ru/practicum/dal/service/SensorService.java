package ru.practicum.dal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dal.model.Sensor;
import ru.practicum.dal.model.mapper.SensorMapper;
import ru.practicum.dal.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SensorService {
    private final SensorRepository sensorRepository;

    @Transactional
    public Sensor save(String hubId, DeviceAddedEventAvro deviceAvro) {
        Sensor sensor = SensorMapper.mapSensor(hubId, deviceAvro.getId());
        return sensorRepository.save(sensor);
    }

    @Transactional
    public void deleteByIdAndHubId(String sensorId, String hubId) {
        sensorRepository.deleteByIdAndHubId(sensorId, hubId);
    }

    public boolean existsByIdInAndHubId(Collection<String> ids, String hubId) {
        return sensorRepository.existsByIdInAndHubId(ids, hubId);
    }

    public Optional<Sensor> findByIdAndHubId(String id, String hubId) {
        return sensorRepository.findByIdAndHubId(id, hubId);
    }
}
