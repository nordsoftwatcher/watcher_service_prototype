package ru.nord.siwatch.backend.connectors.event.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class Event {

    private Long id;

    private Long supervisorId;

    private LocalDateTime deviceTime;

    private LocalDateTime recordTime;

    private String eventType;

    private String eventValue;

    private Double latitude;

    private Double longitude;

}
