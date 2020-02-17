package edu.ucsd.cse110.walkwalkrevolution;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class SerializableSystemClock extends Clock implements Serializable {

    @Override
    public ZoneId getZone() {
        return ZoneId.systemDefault();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return Clock.fixed(instant(), zone);
    }

    @Override public Instant instant() {
        return Instant.now();
    }
}
