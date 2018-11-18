package ru.nord.backend.infrastructure.utils;

import java.time.Duration;
import java.time.Instant;

public class TimeInterval
{
    public static TimeInterval ZERO = new TimeInterval(Instant.now(), Duration.ZERO);

    private final Instant begin;
    private final Duration duration;

    public TimeInterval(Instant begin, Duration duration)
    {
        this.begin = begin;
        this.duration = duration;
    }

    public TimeInterval(Duration duration)
    {
        this.begin = Instant.now().minus(duration);
        this.duration = duration;
    }

    public TimeInterval(Instant i1, Instant i2)
    {
        if(i1.isAfter(i2)) {
            Instant t = i1;
            i1 = i2;
            i2 = t;
        }
        this.begin = i1;
        this.duration = Duration.between(i1, i2);
    }

    public Instant getBegin() {
        return this.begin;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public Instant getEnd() {
        return begin.plus(duration);
    }

    public TimeInterval plus(Duration duration) {
        return new TimeInterval(begin, duration.plus(duration));
    }

    public Instant getMiddle() {
        return begin.plus(duration.dividedBy(2));
    }

    public boolean contains(TimeInterval i) {
        return this.getBegin().isBefore(i.getBegin()) && this.getEnd().isAfter(i.getEnd());
    }

    public static TimeInterval intersect(TimeInterval i1, TimeInterval i2) {
        if(i2.getBegin().isAfter(i1.getEnd())) {
            return new TimeInterval(i2.getBegin(), Duration.ZERO);
        }
        else if(i2.getEnd().isBefore(i1.getBegin())) {
            return new TimeInterval(i1.getBegin(), Duration.ZERO);
        }
        else if(i1.contains(i2)) {
            return i2;
        }
        else if(i2.contains(i1)) {
            return i1;
        }
        else {
            if(i1.getBegin().isBefore(i2.getBegin())) {
                return new TimeInterval(i2.getBegin(), i1.getEnd());
            }
            else {
                return new TimeInterval(i1.getBegin(), i2.getEnd());
            }
        }
    }
}
