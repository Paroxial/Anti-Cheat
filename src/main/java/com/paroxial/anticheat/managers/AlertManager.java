package com.paroxial.anticheat.managers;

import io.netty.util.internal.ConcurrentSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class AlertManager {
    private final Set<UUID> alertedPlayerIds = new ConcurrentSet<>();

    public void receieveAlerts(Player player) {
        alertedPlayerIds.add(player.getUniqueId());
    }

    public void removeAlerted(Player player) {
        alertedPlayerIds.remove(player.getUniqueId());
    }

    public boolean toggleAlerts(Player player) {
        boolean alertsShown = alertedPlayerIds.contains(player.getUniqueId());

        if (!alertsShown) {
            receieveAlerts(player);
        } else {
            removeAlerted(player);
        }

        return !alertsShown;
    }

    public void alertPlayers(String alert) {
        alertedPlayerIds.stream()
                .map(Bukkit::getPlayer).filter(Objects::nonNull).filter(Player::isOnline)
                .forEach(player -> player.sendMessage(alert));
    }
}
