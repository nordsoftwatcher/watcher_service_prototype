package ru.nord.backend.infrastructure.diagnostics;

public class DiagnosticException extends Exception {
    public DiagnosticException() {
    }

    public DiagnosticException(String s) {
        super(s);
    }

    public DiagnosticException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DiagnosticException(Throwable throwable) {
        super(throwable);
    }
}
