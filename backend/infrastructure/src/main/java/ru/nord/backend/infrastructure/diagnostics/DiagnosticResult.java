package ru.nord.backend.infrastructure.diagnostics;

public class DiagnosticResult
{
    private String message;
    private final boolean success;
    private final Exception error;
    private final long duration;

    public DiagnosticResult(boolean success, long duration) {
        this(success, "", null, duration);
    }

    public DiagnosticResult(boolean success, String message, long duration) {
        this(success, message, null, duration);
    }

    public DiagnosticResult(boolean success, String message, Exception error, long duration) {
        this.success = success;
        this.message = message;
        this.error = error;
        this.duration = duration;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean isFailure() {
        return !this.success;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getError() {
        return this.error;
    }

    public long getDuration() {
        return this.duration;
    }
}
