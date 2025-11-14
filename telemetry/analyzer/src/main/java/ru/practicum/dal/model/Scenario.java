package ru.practicum.dal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "scenarios")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Scenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hub_id", nullable = false)
    @NotBlank
    private String hubId;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(
            table = "scenario_conditions",
            name = "sensor_id"
    )
    @JoinTable(
            name = "scenario_conditions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_id")
    )
    @ToString.Exclude
    private Map<Sensor, Condition> sensorConditions = new HashMap<>();

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyJoinColumn(name = "sensor_id")
    @JoinTable(
            name = "scenario_actions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id")
    )
    @ToString.Exclude
    private Map<Sensor, Action> sensorActions;

    public void addSensorCondition(Sensor sensor, Condition condition) {
        sensorConditions.put(sensor, condition);
    }

    public void removeSensorCondition(Sensor sensor) {
        sensorConditions.remove(sensor);
    }

    public void addSensorAction(Sensor sensor, Action action) {
        sensorActions.put(sensor, action);
    }

    public void removeSensorAction(Sensor sensor) {
        sensorActions.remove(sensor);
    }

    public Condition getConditionForSensor(Sensor sensor) {
        return sensorConditions.get(sensor);
    }

    public Action getActionForSensor(Sensor sensor) {
        return sensorActions.get(sensor);
    }
}
