package com.paroxial.anticheat.check.checks.combat.autoclicker;

import com.google.common.util.concurrent.AtomicDouble;
import com.paroxial.anticheat.check.Check;
import com.paroxial.anticheat.packet.PacketDirection;
import com.paroxial.anticheat.packet.WrappedPacket;
import com.paroxial.anticheat.player.PlayerData;
import com.paroxial.anticheat.utils.PacketUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks if the standard deviation of a player's swinging rate is repeatedly the same from a set sample.
 * This detects auto-clickers which always randomly fluctuate with the same variance.
 */
public class SwingDeviation extends Check {
    private static final int SAMPLE_SIZE = 200;

    private final List<Integer> delays = new ArrayList<>();

    private Double lastStandardDeviation;
    private int swingDelay;

    public SwingDeviation(PlayerData data) {
        super("Swing Deviation", 10, data, PacketDirection.INBOUND);
    }

    @Override
    public void handlePacket(WrappedPacket packet) {
        // We only want to process swings when a player isn't digging/placing
        if (packet.hasName("PacketPlayInArmAnimation") && !data.getStats().isDigging() && !data.getStats().isPlacing()) {
            // Only handle delays that aren't in the same tick and aren't too long
            if (swingDelay > 0 && swingDelay < 10) {
                delays.add(swingDelay);
            }

            if (delays.size() == SAMPLE_SIZE) {
                // Calculate mean, variance, and standard deviation
                double averageDelay = delays.stream()
                        .mapToDouble(Integer::doubleValue)
                        .average()
                        .orElse(0.0);

                AtomicDouble variance = new AtomicDouble(0.0);
                delays.forEach(recentDelay -> variance.getAndAdd(Math.pow(recentDelay - averageDelay, 2.0)));
                double deviation = Math.sqrt(variance.get());

                // Check standard deviation
                if (lastStandardDeviation != null && lastStandardDeviation == deviation) {
                    double swingFrequency = 20.0 / averageDelay;
                    trigger(String.format("Deviation: %.3f, Frequency: %.1f", deviation, swingFrequency));
                } else {
                    violations = Math.max(0, violations - 1);
                }

                lastStandardDeviation = deviation;

                delays.clear();
            }

            swingDelay = 0;
        } else if (PacketUtils.isFlyingPacket(packet)) {
            ++swingDelay;
        }
    }
}
