package com.paroxial.anticheat.check.checks.combat.autoclicker;

import com.paroxial.anticheat.check.Check;
import com.paroxial.anticheat.packet.PacketDirection;
import com.paroxial.anticheat.packet.WrappedPacket;
import com.paroxial.anticheat.player.PlayerData;

/**
 * Checks if a player stops digging a block without swinging.
 * This detects auto-clickers which have no delay between mouse down and mouse up.
 */
public class InvalidDig extends Check {
    private boolean digging;
    private int digCount = 0;

    public InvalidDig(PlayerData data) {
        super("Invalid Dig", 10, data, PacketDirection.INBOUND);
    }

    @Override
    public void handlePacket(WrappedPacket packet) {
        if (packet.hasName("PacketPlayInBlockDig")) {
            Enum<?> diggingState = packet.getField("c");

            // The player has started digging
            if (diggingState.name().equals("START_DESTROY_BLOCK")) {
                digging = true;
            } else if (diggingState.name().equals("ABORT_DESTROY_BLOCK")) {
                if (digging) {
                    if (++digCount > 20) {
                        trigger();
                    }
                } else {
                    digCount = 0;
                }
            }
        } else if (packet.hasName("PacketPlayInArmAnimation")) {
            // The player has swung, which means the normal packet sequence has happened
            digging = false;
        }
    }
}
