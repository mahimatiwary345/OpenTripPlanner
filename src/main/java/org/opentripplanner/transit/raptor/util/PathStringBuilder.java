package org.opentripplanner.transit.raptor.util;

import org.opentripplanner.routing.core.TraverseMode;

import java.util.Calendar;

/**
 * Create a path like: {@code Walk 5m - 101 - Transit 10:07 10:35 - 2111 - Walk 4m }
 */
public class PathStringBuilder {
    private final StringBuilder buf = new StringBuilder();
    private final boolean padDuration;

    public PathStringBuilder() {
        this(false);
    }
    public PathStringBuilder(boolean padDuration) {
        this.padDuration = padDuration;
    }

    public PathStringBuilder sep() {
        return append(" ~ ");
    }

    public PathStringBuilder stop(int stop) {
        return append(stop);
    }

    public PathStringBuilder stop(String stop) {
        return append(stop);
    }

    public PathStringBuilder walk(int duration) {
        return append("Walk ").duration(duration);
    }

    public PathStringBuilder transit(String description, int fromTime, int toTime) {
        return append(description).append(" ").time(fromTime, toTime);
    }

    public PathStringBuilder transit(TraverseMode mode, String trip, Calendar fromTime, Calendar toTime) {
        return append(mode.name()).append(" ").append(trip).append(" ").time(fromTime, toTime);
    }

    public PathStringBuilder other(TraverseMode mode, Calendar fromTime, Calendar toTime) {
        return append(mode.name()).append(" ").append(" ").time(fromTime, toTime);
    }


    @Override
    public String toString() {
        return buf.toString();
    }


    /* private helpers */

    private PathStringBuilder duration(int duration) {
        String durationStr = TimeUtils.durationToStr(duration);
        return append(padDuration ? String.format("%5s", durationStr) : durationStr);
    }

    private PathStringBuilder time(int from, int to) {
        return append(TimeUtils.timeToStrCompact(from))
                .append(" ")
                .append(TimeUtils.timeToStrCompact(to));
    }

    private PathStringBuilder time(Calendar from, Calendar to) {
        return append(TimeUtils.timeToStrCompact(from))
            .append(" ")
            .append(TimeUtils.timeToStrCompact(to));
    }

    private PathStringBuilder append(String text) {
        buf.append(text);
        return this;
    }

    private PathStringBuilder append(int value) {
        buf.append(value);
        return this;
    }
}
