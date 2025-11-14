package ru.practicum.dal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dal.model.Scenario;
import ru.practicum.dal.repository.ScenarioRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;

    @Transactional
    public Scenario save(Scenario scenario) {
        return scenarioRepository.save(scenario);
    }

    @Transactional
    public Scenario delete(Scenario scenario) {
        scenarioRepository.delete(scenario);
        return scenario;
    }

    public List<Scenario> findByHubId(String hubId) {
        return scenarioRepository.findByHubId(hubId);
    }

    public Optional<Scenario> findByHubIdAndName(String hubId, String name) {
        return scenarioRepository.findByHubIdAndName(hubId, name);
    }
}
