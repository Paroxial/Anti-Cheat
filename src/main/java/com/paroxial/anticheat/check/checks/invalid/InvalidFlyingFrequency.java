package com.paroxial.anticheat.check.checks.invalid;

import com.paroxial.anticheat.check.Check;
import com.paroxial.anticheat.packet.WrappedPacket;
import com.paroxial.anticheat.player.PlayerData;
import com.paroxial.anticheat.utils.PacketUtils;

/*
 * Checks if a player sends more than 20 flying packets without updating their position.
 * This can't be checked while in a vehicle.
 */
public class InvalidFlyingFrequency extends Check {
    private int flyingFrequency;

    public InvalidFlyingFrequency(PlayerData data) {
        super("Flying Frequency", 5, data);
    }

    @Override
    public void handlePacket(WrappedPacket packet) {
        if (PacketUtils.isFlyingPacket(packet)) {
            if (PacketUtils.flyingHasPosition(packet) || data.getPlayer().isInsideVehicle()) {
                flyingFrequency = 0;
            } else if (++flyingFrequency > 20) {
                if (!data.getPlayer().isInsideVehicle()) {
                    trigger("Frequency: " + flyingFrequency);
                }
            }
        }
    }
}
