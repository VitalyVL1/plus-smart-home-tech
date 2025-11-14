package ru.practicum.dal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dal.model.Scenario;
import ru.practicum.dal.model.mapper.ScenarioMapper;
import ru.practicum.dal.repository.ScenarioRepository;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;

    public Scenario saveOrUpdate(String hubId, ScenarioAddedEventAvro scenarioAvro) {
        Scenario newScenario = ScenarioMapper.mapScenario(hubId, scenarioAvro);
        Optional<Scenario> existingScenario = scenarioRepository.findByHubIdAndName(hubId, newScenario.getName());

        Scenario savedScenario;
        if (existingScenario.isPresent()) {
            savedScenario = existingScenario.get();
            updateScenarioData(savedScenario, newScenario);
            log.info("Updated scenario: {} for hub: {}", newScenario.getName(), hubId);
        } else {
            savedScenario = newScenario;
            log.info("Created new scenario: {} for hub: {}", newScenario.getName(), hubId);
        }

        return scenarioRepository.save(savedScenario);
    }


    @Transactional
    public void deleteByHubIdAndName(String hubId, String scenarioName) {
        scenarioRepository.deleteByHubIdAndName(hubId, scenarioName);
    }

    public List<Scenario> findByHubId(String hubId) {
        return scenarioRepository.findByHubId(hubId);
    }

    private void updateScenarioData(Scenario existing, Scenario newData) {
        // Очищаем старые коллекции
        existing.getSensorConditions().clear();
        existing.getSensorActions().clear();

        // Добавляем новые данные через putAll()
        existing.getSensorConditions().putAll(newData.getSensorConditions());
        existing.getSensorActions().putAll(newData.getSensorActions());
    }
}
