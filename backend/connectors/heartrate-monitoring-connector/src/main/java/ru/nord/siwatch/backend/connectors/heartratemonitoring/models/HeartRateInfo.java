package ru.nord.siwatch.backend.connectors.heartratemonitoring.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class HeartRateInfo
{
    private String deviceId;
    private LocalDateTime deviceTime;
    private float rate;
}
