package ru.nord.backend.infrastructure.diagnostics;

import java.util.HashMap;
import java.util.Map;

public class DiagnosticContext
{
    private final Map<String, Object> context = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T get(String key)
    {
        return (T)context.get(key);
    }

    public <T> void set(String key, T value)
    {
        context.put(key, value);
    }
}
