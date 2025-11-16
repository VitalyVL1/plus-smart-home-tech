package ru.practicum.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dal.model.Condition;

/**
 * Репозиторий для управления сущностями Condition в базе данных.
 * Предоставляет базовые CRUD-операции через JpaRepository.
 */
public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
