package ru.practicum.dal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dal.model.Sensor;
import ru.practicum.dal.repository.SensorRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SensorService {
    private final SensorRepository sensorRepository;

    @Transactional
    public Sensor save(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    @Transactional
    public Sensor delete(Sensor sensor) {
        sensorRepository.delete(sensor);
        return sensor;
    }

    public boolean existsByIdInAndHubId(Collection<String> ids, String hubId) {
        return sensorRepository.existsByIdInAndHubId(ids, hubId);
    }

    public Optional<Sensor> findByIdAndHubId(String id, String hubId) {
        return sensorRepository.findByIdAndHubId(id, hubId);
    }
}
