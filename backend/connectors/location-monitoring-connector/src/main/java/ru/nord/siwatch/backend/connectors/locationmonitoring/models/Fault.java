package ru.nord.siwatch.backend.connectors.locationmonitoring.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Fault
{
    private String code;
    private String message;
    private String details;
}
