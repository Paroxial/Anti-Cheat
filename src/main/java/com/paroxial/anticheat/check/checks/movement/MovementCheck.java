package com.paroxial.anticheat.check.checks.movement;

import com.paroxial.anticheat.check.Check;
import com.paroxial.anticheat.packet.PacketDirection;
import com.paroxial.anticheat.packet.WrappedPacket;
import com.paroxial.anticheat.player.PlayerData;
import com.paroxial.anticheat.utils.PacketUtils;
import org.bukkit.Location;

public abstract class MovementCheck extends Check {
    private Location last;

    public MovementCheck(String name, PlayerData data) {
        super(name, -1, data, PacketDirection.INBOUND, PacketDirection.OUTBOUND);
    }

    public MovementCheck(String name, int maxViolations, PlayerData data) {
        super(name, maxViolations, data, PacketDirection.INBOUND, PacketDirection.OUTBOUND);
    }

    @Override
    public void handlePacket(WrappedPacket packet) {
        if (PacketUtils.isFlyingPacket(packet) && PacketUtils.flyingHasPosition(packet)) {
            Location current = PacketUtils.getLocationFromFlyingPacket(packet, data.getPlayer().getWorld());

            if (last != null) {
                handleMovement(current, last);
            }

            last = current;
        }
    }

    public abstract void handleMovement(Location to, Location from);
}
