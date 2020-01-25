package com.paroxial.anticheat.listeners;

import com.paroxial.anticheat.AntiCheatPlugin;
import com.paroxial.anticheat.Theme;
import com.paroxial.anticheat.check.event.CheckBanEvent;
import com.paroxial.anticheat.check.event.CheckFlagEvent;
import com.paroxial.anticheat.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CheckListener implements Listener {
    private final AntiCheatPlugin plugin = AntiCheatPlugin.getInstance();

    @EventHandler
    public void onFlag(CheckFlagEvent event) {
        if (!event.isAlerting()) {
            return;
        }

        plugin.getAlertManager().alertPlayers(Theme.PREFIX + event.getMessage());
    }


    @EventHandler
    public void onBan(CheckBanEvent event) {
        if (!plugin.isBanningEnabled()) {
            return;
        }

        Player player = event.getCheck().getData().getPlayer();
        PlayerData data = plugin.getPlayerDataManager().getData(player);

        data.getPlayerBanned().set(true);

        String command = "ban " + player.getName() + " " + event.getReason();

        // This event is being called async, and we need to run commands sync
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            Bukkit.broadcastMessage(Theme.PREFIX + player.getDisplayName() + Theme.SECONDARY + " has been detected " +
                    "cheating and banned from this server!");
        });
    }
}
