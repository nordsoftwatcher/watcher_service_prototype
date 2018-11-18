package ru.nord.backend.infrastructure.diagnostics;

public class DiagnosticItem {
    private Diagnostic diagnostic;
    private long timeout;
    private String title;
    private DiagnosticResult result;

    public DiagnosticResult getResult()
    {
        return result;
    }

    public void setResult(DiagnosticResult result)
    {
        this.result = result;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public long getTimeout()
    {
        return timeout;
    }

    public void setTimeout(long timeout)
    {
        this.timeout = timeout;
    }

    public Diagnostic getDiagnostic()
    {
        return diagnostic;
    }

    public void setDiagnostic(Diagnostic diagnostic)
    {
        this.diagnostic = diagnostic;
    }
}
