package ru.practicum.dal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "conditions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private ConditionType type;

    @Column(nullable = false)
    @NotBlank
    private ConditionOperation operation;

    @Column
    private Integer value;
}
