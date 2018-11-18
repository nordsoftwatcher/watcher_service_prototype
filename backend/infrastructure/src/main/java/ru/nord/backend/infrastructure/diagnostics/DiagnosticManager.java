package ru.nord.backend.infrastructure.diagnostics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class DiagnosticManager {
    private final List<DiagnosticItem> items = new ArrayList<>();

    public void addDiagnostic(Diagnostic d, String title, long timeout)
    {
        DiagnosticItem item = new DiagnosticItem();
        item.setDiagnostic(d);
        item.setTitle(title);
        item.setTimeout(timeout);
        items.add(item);
    }

    public void run(DiagnosticContext context)
    {
        final DiagnosticRunner runner = new DiagnosticRunner();
        final List<Future<DiagnosticResult>> results = items.stream()
                .map(d -> runner.run(d.getDiagnostic(), context, d.getTimeout(), false))
                .collect(Collectors.toList());

        runner.waitAll(60*1000);

        for(int i=0; i<items.size(); i++) {
            DiagnosticResult result;
            try {
                result = results.get(i).get();
            }
            catch (InterruptedException | ExecutionException ex) {
                result = new DiagnosticResult(false, "СТРАННАЯ ОШИБКА", ex, 0);
            }
            items.get(i).setResult(result);
        }
    }

    public List<DiagnosticItem> diagnostics() {
        return Collections.unmodifiableList(this.items);
    }
}
