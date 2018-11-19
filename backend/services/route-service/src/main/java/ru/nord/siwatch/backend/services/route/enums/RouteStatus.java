package ru.nord.siwatch.backend.services.route.enums;

public enum  RouteStatus {

    NOT_STARTED("NOT_STARTED", "Не начался"),
    IN_PROGRESS("IN_PROGRESS", "В пути"),
    DONE("DONE", "Завершен");

    private String value;
    private String description;

    RouteStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
