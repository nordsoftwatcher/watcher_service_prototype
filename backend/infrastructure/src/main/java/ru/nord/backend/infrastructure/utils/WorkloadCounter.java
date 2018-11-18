package ru.nord.backend.infrastructure.utils;

import java.time.Duration;
import java.time.Instant;

public class WorkloadCounter
{
    private final Duration interval;

    private TimeInterval previous;
    private TimeInterval current = null;

    public WorkloadCounter(Duration interval)
    {
        this.interval = interval;
        previous = new TimeInterval(Instant.now(), Duration.ZERO);
    }

    public synchronized void begin() {
        current = new TimeInterval(Instant.now(), Duration.ZERO);
    }

    public synchronized void end() {
        if(current == null) {
            return;
        }
        TimeInterval window = new TimeInterval(interval);
        Duration elapsed = Duration.between(current.getBegin(), Instant.now());
        previous = TimeInterval.intersect(window, previous).plus(TimeInterval.intersect(window, current.plus(elapsed)).getDuration());
        current = null;
    }

    public synchronized double getWorkloadFactor() {
        TimeInterval c = TimeInterval.ZERO;
        if(current != null) {
            Duration elapsed = Duration.between(current.getBegin(), Instant.now());
            c = current.plus(elapsed);
        }

        TimeInterval window = new TimeInterval(interval);
        Duration busy = TimeInterval.intersect(window, previous).getDuration().plus(TimeInterval.intersect(window, c).getDuration());
        return (double)(busy.toMillis())/window.getDuration().toMillis();
    }
}
