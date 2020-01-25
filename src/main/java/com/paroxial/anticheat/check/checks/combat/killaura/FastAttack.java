package com.paroxial.anticheat.check.checks.combat.killaura;

import com.paroxial.anticheat.check.Check;
import com.paroxial.anticheat.packet.PacketDirection;
import com.paroxial.anticheat.packet.WrappedPacket;
import com.paroxial.anticheat.player.PlayerData;
import com.paroxial.anticheat.utils.PacketUtils;

/**
 * Checks if a player is attacking faster than a set threshold every second.
 */
public class FastAttack extends Check {
    private static final int ATTACK_FREQUENCY_LIMIT = 20;

    private int attacks;
    private int clientTicks;

    public FastAttack(PlayerData data) {
        super("Fast Attack", 5, data, PacketDirection.INBOUND);
    }

    @Override
    public void handlePacket(WrappedPacket packet) {
        if (packet.hasName("PacketPlayInUseEntity")) {
            Enum<?> value = packet.getField("action");

            if (value.name().equals("ATTACK")) {
                attacks++;
            }
        } else if (PacketUtils.isFlyingPacket(packet)) {
            // A second has passed, so check the player's attack frequency
            if (++clientTicks == 20) {
                if (attacks > ATTACK_FREQUENCY_LIMIT) {
                    // Only trigger a violation if the player is not lagging
                    if (!data.getStats().isLagging()) {
                        trigger("Attacks: " + attacks);
                    }
                }

                attacks = clientTicks = 0;
            }
        }
    }
}
