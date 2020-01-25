package com.paroxial.anticheat.check.checks.combat.killaura;

import com.paroxial.anticheat.check.Check;
import com.paroxial.anticheat.packet.PacketDirection;
import com.paroxial.anticheat.packet.WrappedPacket;
import com.paroxial.anticheat.player.PlayerData;
import com.paroxial.anticheat.utils.PacketUtils;

/**
 * Checks if a player attacked without swinging prior.
 */
public class NoSwing extends Check {
    private boolean swung;

    public NoSwing(PlayerData data) {
        super("No Swing", 10, data, PacketDirection.INBOUND);
    }

    @Override
    public void handlePacket(WrappedPacket wrapper) {
        if (wrapper.hasName("PacketPlayInUseEntity")) {
            Enum<?> value = wrapper.getField("action");

            if (value.name().equals("ATTACK")) {
                if (!swung) {
                    trigger();
                } else {
                    swung = false;
                }
            }
        } else if (wrapper.hasName("PacketPlayInArmAnimation")) {
            swung = true;
        } else if (PacketUtils.isFlyingPacket(wrapper)) {
            swung = false;
        }
    }
}
