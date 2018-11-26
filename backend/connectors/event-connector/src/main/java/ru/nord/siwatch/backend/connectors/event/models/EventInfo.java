package ru.nord.siwatch.backend.connectors.event.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class EventInfo {

    private Long supervisorId;

    private LocalDateTime deviceTime;

    private String eventType;

    private String eventValue;

    private Double latitude;

    private Double longitude;

}
