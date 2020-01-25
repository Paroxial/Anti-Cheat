package com.paroxial.anticheat.check.checks.combat.autoclicker;

import com.paroxial.anticheat.check.Check;
import com.paroxial.anticheat.packet.PacketDirection;
import com.paroxial.anticheat.packet.WrappedPacket;
import com.paroxial.anticheat.player.PlayerData;
import com.paroxial.anticheat.utils.PacketUtils;

/**
 * Checks if a player is swinging faster than a set threshold every second.
 */
public class FastSwing extends Check {
    private static final int SWING_FREQUENCY_LIMIT = 20;

    private int swings;
    private int clientTicks;

    public FastSwing(PlayerData data) {
        super("Fast Swing", 5, data, PacketDirection.INBOUND);
    }

    @Override
    public void handlePacket(WrappedPacket packet) {
        // We only want to process swings when a player isn't digging/placing
        if (packet.hasName("PacketPlayInArmAnimation") && !data.getStats().isDigging() && !data.getStats().isPlacing()) {
            swings++;
        } else if (PacketUtils.isFlyingPacket(packet)) {
            // A second has passed, so check the player's swing frequency
            if (++clientTicks == 20) {
                if (swings > SWING_FREQUENCY_LIMIT) {
                    // Only trigger a violation if the player is not lagging
                    if (data.getStats().isLagging()) {
                        trigger("Swings: " + swings);
                    }
                } else {
                    decrementViolations();
                }

                swings = clientTicks = 0;
            }
        }
    }
}
