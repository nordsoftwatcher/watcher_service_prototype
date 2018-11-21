package ru.nord.siwatch.backend.connectors.memorymonitoring.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Memory {

    private Long id;

    private String deviceId;

    private LocalDateTime deviceTime;

    private Long freeSystemMemory;

    private Long freeStorageMemory;
}
