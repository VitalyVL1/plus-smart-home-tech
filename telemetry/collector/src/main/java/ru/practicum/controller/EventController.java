package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.sensor.SensorEvent;

@RestController("/events")
@Validated
@Slf4j
@RequiredArgsConstructor
public class EventController {

    @PostMapping("/hubs")
    @ResponseStatus(HttpStatus.OK)
    public void collectSensorEvent(@RequestBody @Valid SensorEvent sensorEvent) {
        log.info("collectSensorEvent = {}", sensorEvent);

    }

    @PostMapping("/sensors")
    @ResponseStatus(HttpStatus.OK)
    public void collectHubEvent(@RequestBody @Valid HubEvent hubEvent) {
        log.info("collectHubEvent = {}", hubEvent);

    }


}
