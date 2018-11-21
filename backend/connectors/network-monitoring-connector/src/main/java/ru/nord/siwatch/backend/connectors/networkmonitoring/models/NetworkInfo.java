package ru.nord.siwatch.backend.connectors.networkmonitoring.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class NetworkInfo {

    private String deviceId;

    private LocalDateTime deviceTime;

    private Boolean available;
}
