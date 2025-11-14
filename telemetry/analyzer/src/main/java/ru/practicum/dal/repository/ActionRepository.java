package ru.practicum.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dal.model.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
