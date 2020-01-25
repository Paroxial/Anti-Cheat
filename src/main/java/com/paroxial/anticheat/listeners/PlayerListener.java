package com.paroxial.anticheat.listeners;

import com.paroxial.anticheat.AntiCheatPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final AntiCheatPlugin plugin = AntiCheatPlugin.getInstance();

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.getPlayerDataManager().createData(player);

        if (player.hasPermission("anticheat.alert")) {
            plugin.getAlertManager().receieveAlerts(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Cleanup
        plugin.getPlayerDataManager().removeData(player);
        plugin.getAlertManager().removeAlerted(player);
    }
}
