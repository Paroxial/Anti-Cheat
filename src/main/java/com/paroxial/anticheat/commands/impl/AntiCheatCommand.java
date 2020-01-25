package com.paroxial.anticheat.commands.impl;

import com.paroxial.anticheat.AntiCheatPlugin;
import com.paroxial.anticheat.commands.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AntiCheatCommand implements BaseCommand {
    private static final String USAGE = ChatColor.RED + "Usage: /anticheat <alerts|autoban>";
    private final AntiCheatPlugin plugin = AntiCheatPlugin.getInstance();

    @Override
    public String getName() {
        return "anticheat";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(USAGE);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "alerts":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "The Console can't receive alerts.");
                    return true;
                }

                Player player = (Player) sender;

                boolean showingAlerts = plugin.getAlertManager().toggleAlerts(player);
                player.sendMessage(showingAlerts ?
                        ChatColor.GREEN + "You can now see alerts!" :
                        ChatColor.RED + "You can no longer see alerts.");
                break;
            case "autoban":
                boolean banningEnabled = !plugin.isBanningEnabled();
                plugin.setBanningEnabled(banningEnabled);

                sender.sendMessage(banningEnabled ?
                        ChatColor.GREEN + "Auto-banning has been enabled!" :
                        ChatColor.RED + "Auto-banning has been disabled.");
                break;
            default:
                sender.sendMessage(USAGE);
                break;
        }

        return true;
    }
}
