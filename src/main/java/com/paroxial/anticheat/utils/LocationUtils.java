package com.paroxial.anticheat.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;

@UtilityClass
public class LocationUtils {
    private static final double ONE_SIXTY_FORTH = 0.015625;

    public boolean isOnGround(Location location) {
        return location.getY() % ONE_SIXTY_FORTH == 0.0;
    }
}
