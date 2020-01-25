package com.paroxial.anticheat.check.checks.invalid;

import com.paroxial.anticheat.check.Check;
import com.paroxial.anticheat.packet.PacketDirection;
import com.paroxial.anticheat.packet.WrappedPacket;
import com.paroxial.anticheat.player.PlayerData;
import com.paroxial.anticheat.utils.PacketUtils;

/*
 * Checks if the player's pitch is above 90 degrees which is an impossible angle.
 */
public class InvalidPitch extends Check {
    public InvalidPitch(PlayerData data) {
        super("Invalid Pitch", 1, data, PacketDirection.INBOUND);
    }

    @Override
    public void handlePacket(WrappedPacket packet) {
        if (!PacketUtils.isFlyingPacket(packet)) {
            return;
        }

        float pitch = PacketUtils.getFlyingPitch(packet);

        if (Math.abs(pitch) > 90.0F) {
            trigger();
        }
    }
}
