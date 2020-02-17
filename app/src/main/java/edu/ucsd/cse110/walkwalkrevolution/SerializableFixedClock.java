package edu.ucsd.cse110.walkwalkrevolution;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class SerializableFixedClock extends Clock implements Serializable {

    private Instant initialInstant;
    private long fixedMillis;

    public SerializableFixedClock(int fixedSeconds) {
        fixedMillis = fixedSeconds * MeasurementConverter.MILLIS_IN_SEC;
    }

    public SerializableFixedClock(long fixedMillis) {
        this.fixedMillis = fixedMillis;
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
        return initialInstant.plusMillis(fixedMillis);
    }
}