package com.paroxial.anticheat.check.checks.invalid;

import com.paroxial.anticheat.check.Check;
import com.paroxial.anticheat.packet.PacketDirection;
import com.paroxial.anticheat.packet.WrappedPacket;
import com.paroxial.anticheat.player.PlayerData;
import com.paroxial.anticheat.utils.PacketUtils;

/**
 * Checks if a player sends a sneak update twice without their client ticking first.
 */
public class InvalidSneak extends Check {
    private boolean sentPacket;

    public InvalidSneak(PlayerData data) {
        super("Invalid Sneak", 10, data, PacketDirection.INBOUND);
    }

    @Override
    public void handlePacket(WrappedPacket packet) {
        if (packet.hasName("PacketPlayInEntityAction")) {
            Enum<?> animation = packet.getField("animation");

            if (animation.name().equals("START_SNEAKING") || animation.name().equals("STOP_SNEAKING")) {
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
