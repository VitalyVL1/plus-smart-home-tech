package ru.practicum.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dal.model.Condition;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
