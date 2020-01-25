package com.paroxial.anticheat.check.checks.invalid;

import com.paroxial.anticheat.check.Check;
import com.paroxial.anticheat.packet.PacketDirection;
import com.paroxial.anticheat.packet.WrappedPacket;
import com.paroxial.anticheat.player.PlayerData;
import com.paroxial.anticheat.utils.PacketUtils;

/**
 * Checks if a player sends a sprint update twice without their client ticking first.
 */
public class InvalidSprint extends Check {
    private boolean sentPacket;

    public InvalidSprint(PlayerData data) {
        super("Invalid Sprint", 10, data, PacketDirection.INBOUND);
    }

    @Override
    public void handlePacket(WrappedPacket packet) {
        if (packet.hasName("PacketPlayInEntityAction")) {
            Enum<?> animation = packet.getField("animation");

            if (animation.name().equals("START_SPRINTING") || animation.name().equals("STOP_SPRINTING")) {
                if (sentPacket) {
                    // Don't trigger if they're teleporting (client may send invalid packets)
                    if (!data.getStats().isTeleporting()) {
                        trigger();
                    }
                } else {
                    sentPacket = true;
                }
            }
        } else if (PacketUtils.isFlyingPacket(packet)) {
            sentPacket = false;
        }
    }
}
