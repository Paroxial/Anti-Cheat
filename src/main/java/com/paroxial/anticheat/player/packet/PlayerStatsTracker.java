package com.paroxial.anticheat.player.packet;

import com.paroxial.anticheat.packet.WrappedPacket;
import com.paroxial.anticheat.packet.handler.PacketHandler;
import com.paroxial.anticheat.player.PlayerData;
import com.paroxial.anticheat.player.PlayerStats;
import com.paroxial.anticheat.utils.PacketUtils;
import com.paroxial.anticheat.utils.TimeUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

@Getter
@RequiredArgsConstructor
public class PlayerStatsTracker implements PacketHandler {
    public final PlayerData data;

    @Override
    public void handlePacket(final WrappedPacket packet) {
        final long currentTime = TimeUtils.getMonotonicMillis();
        PlayerStats stats = data.getStats();

        if (PacketUtils.isFlyingPacket(packet)) {
            stats.setLastFlyingTime(currentTime);

            long flyingDelta = currentTime - data.getStats().getLastFlyingTime();

            if (flyingDelta >= 125L) {
                data.getStats().setLastDelayedFlyingTime(currentTime);
            }

            if (currentTime - stats.getLastTeleportTime() > 10_000) {
                stats.setTeleporting(false);
            }

            if (stats.isTeleporting()) {
                Location flyingLocation = PacketUtils.getLocationFromFlyingPacket(packet, data.getPlayer().getWorld());
                Location last = stats.getLastTeleportLocation();

                if (last != null && flyingLocation != null) {
                    if (last.distanceSquared(flyingLocation) < 3E-6) {
                        stats.setTeleporting(false);
                    }
                }
            }
        }

        if (packet.hasName("PacketPlayInKeepAlive")) {
            stats.setLastKeepAliveTime(currentTime);
        }

        if (packet.hasName("PacketPlayInBlockDig")) {
            Enum<?> digType = packet.getField("c");

            switch (digType.name()) {
                case "START_DESTROY_BLOCK":
                    stats.setDigging(true);
                    break;
                case "ABORT_DESTROY_BLOCK":
                case "STOP_DESTROY_BLOCK":
                    stats.setDigging(false);
                    break;
            }
        }

        if (packet.hasName("PacketPlayInEntityAction")) {
            Enum<?> actionType = packet.getField("animation");

            switch (actionType.name()) {
                case "START_SPRINTING":
                    stats.setSprinting(true);
                    break;
                case "STOP_SPRINTING":
                    stats.setSprinting(false);
                    break;
                case "START_SNEAKING":
                    stats.setSneaking(true);
                    break;
                case "STOP_SNEAKING":
                    stats.setSneaking(false);
                    break;
            }
        }

        if (packet.hasName("PacketPlayInBlockPlace")) {
            stats.setPlacing(true);
        } else if (PacketUtils.isFlyingPacket(packet)) {
            stats.setPlacing(false);
        }

        if (packet.hasName("PacketPlayOutPosition")) {
            stats.setTeleporting(true);
            stats.setLastTeleportTime(currentTime);

            double x = packet.getField("a");
            double y = packet.getField("b");
            double z = packet.getField("c");

            stats.setLastTeleportLocation(new Location(data.getPlayer().getWorld(), x, y, z));
        }
    }
}
