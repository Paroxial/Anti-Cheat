package com.paroxial.anticheat.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtils {
    /**
     * Retrieves the elapsed monotonic millseconds since an arbitrary point in time.
     * This is to be used in place of {@link System#currentTimeMillis()} for calculating time deltas.
     *
     * @return monotonic millseconds since an arbitrary point in time
     */
    public long getMonotonicMillis() {
        return System.nanoTime() / 1000000L;
    }
}
