package ru.nord.backend.infrastructure.diagnostics;

public class DummyDiagnostic implements Diagnostic {
    private final long delay;

    public DummyDiagnostic(long delay) {
        this.delay = delay;
    }

    @Override
    public DiagnosticStatus call(DiagnosticContext context) throws Exception {
        Thread.sleep(delay);
        return new DiagnosticStatus(true, "OK");
    }
}
