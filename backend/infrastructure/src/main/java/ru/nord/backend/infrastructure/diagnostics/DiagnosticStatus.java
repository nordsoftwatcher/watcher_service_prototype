package ru.nord.backend.infrastructure.diagnostics;

public class DiagnosticStatus {
    private final boolean success;
    private final String message;

    public DiagnosticStatus(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
