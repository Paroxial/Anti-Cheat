package com.paroxial.anticheat.player;

import com.paroxial.anticheat.utils.TimeUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
@NoArgsConstructor
public class PlayerStats {
    // Player attribute stats
    private boolean placing, digging;
    private boolean sprinting, sneaking;

    // Teleport stats
    private Location lastTeleportLocation;
    private long lastTeleportTime;
    private boolean teleporting;

    // Flying stats
    private long lastDelayedFlyingTime;
    private long lastFlyingTime = TimeUtils.getMonotonicMillis();

    // Keep alive stats
    private long lastKeepAliveTime;

    public boolean isLagging() {
        return (TimeUtils.getMonotonicMillis() - lastFlyingTime) >= 125L ||
                (TimeUtils.getMonotonicMillis() - lastDelayedFlyingTime) <= 225L;
    }
}
