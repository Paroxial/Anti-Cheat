package com.paroxial.anticheat.check.checks.movement.impl;

import com.paroxial.anticheat.check.checks.movement.MovementCheck;
import com.paroxial.anticheat.player.PlayerData;
import com.paroxial.anticheat.utils.LocationUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Checks if a player is floating above the ground.
 */
public class Flight extends MovementCheck {
    public Flight(PlayerData data) {
        super("Flight", data);
    }

    private static boolean ignoreCases(Player player) {
        return player.getGameMode() != GameMode.SURVIVAL || player.getAllowFlight()
                || player.getVelocity().getY() > 0;
    }

    @Override
    public void handleMovement(Location to, Location from) {
        Player player = data.getPlayer();

        if (LocationUtils.isOnGround(to) || LocationUtils.isOnGround(from) || ignoreCases(player)) {
            return;
        }

        double deltaH = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
        double deltaY = to.getY() - from.getY();

        if (deltaH > 0.0 && deltaY >= 0.0 && deltaY < 0.01) {
            trigger(String.format("Delta: %.3f ", deltaY));
        } else {
            clearViolations();
        }
    }
}
