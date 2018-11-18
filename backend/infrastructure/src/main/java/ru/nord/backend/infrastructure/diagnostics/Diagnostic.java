package ru.nord.backend.infrastructure.diagnostics;

public interface Diagnostic {
    DiagnosticStatus call(DiagnosticContext ctx) throws Exception;

    default String getName() {
        return this.toString();
    }
}
