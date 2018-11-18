package ru.nord.backend.infrastructure.utils;

import java.time.Duration;
import java.time.Instant;

public class Stopwatch
{
    private Instant begin;

    private Stopwatch()
    {
        begin = Instant.now();
    }

    public static Stopwatch start() {
        return new Stopwatch();
    }

    public Duration elapsed() {
        return Duration.between(begin, Instant.now());
    }

    public void reset() {
        begin = Instant.now();
    }
}
