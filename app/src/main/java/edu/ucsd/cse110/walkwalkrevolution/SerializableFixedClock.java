package edu.ucsd.cse110.walkwalkrevolution;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class SerializableFixedClock extends Clock implements Serializable {

    private Instant initialInstant;
    private int fixedSeconds;

    public SerializableFixedClock(int fixedSeconds) {
        this.fixedSeconds = fixedSeconds;
    }

    @Override
    public ZoneId getZone() {
        return ZoneId.systemDefault();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return Clock.fixed(instant(), zone);
    }

    @Override public Instant instant() {
        if (initialInstant == null) {
            initialInstant = Instant.now();
            return Instant.now();
        }
        return initialInstant.plusSeconds(fixedSeconds);
    }
}