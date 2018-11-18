package ru.nord.backend.infrastructure.diagnostics;

import ru.nord.backend.infrastructure.utils.Stopwatch;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class DiagnosticRunner {
    private class DiagnosticThread extends Thread
    {
        private final Diagnostic diagnostic;
        private final DiagnosticContext context;
        private final AtomicReference<DiagnosticResult> result = new AtomicReference<>();

        public DiagnosticThread(Diagnostic diagnostic, DiagnosticContext context) {
            this.diagnostic = diagnostic;
            this.context = context;
        }

        @Override
        public void run() {
            final Stopwatch stopwatch = Stopwatch.start();
            final DiagnosticStatus status;
            try {
                status = this.diagnostic.call(context);
            }
            catch (Exception ex) {
                result.set(new DiagnosticResult(false, "", ex, stopwatch.elapsed().toMillis()));
                return;
            }
            result.set(new DiagnosticResult(status.isSuccess(), status.getMessage(), null, stopwatch.elapsed().toMillis()));
        }

        public DiagnosticResult getResult() {
            return result.get();
        }
    }

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public Future<DiagnosticResult> run(Diagnostic diagnostic, DiagnosticContext context, long timeout, boolean safeToInterrupt)
    {
        return executor.submit(() -> {
            final DiagnosticThread dt = new DiagnosticThread(diagnostic, context);
            dt.start();
            dt.join(timeout);

            DiagnosticResult result = dt.getResult();
            if(result == null) {
                result = new DiagnosticResult(false, "Операция не была успешно выполнена за отведенное время", timeout);
            }

            if(safeToInterrupt) {
                dt.interrupt();
            }

            return result;
        });
    }

    public void waitAll(long timeout)
    {
        executor.shutdown();
        try {
            executor.awaitTermination(timeout, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException ex) {
        }
    }
}
