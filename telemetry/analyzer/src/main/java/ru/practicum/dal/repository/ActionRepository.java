package ru.practicum.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dal.model.Action;

/**
 * Репозиторий для управления сущностями Action в базе данных.
 * Предоставляет базовые CRUD-операции через JpaRepository.
 */
public interface ActionRepository extends JpaRepository<Action, Long> {
}
