package com.paroxial.anticheat.managers;

import com.paroxial.anticheat.player.PlayerData;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;

public class PlayerDataManager {
    private final Map<UUID, PlayerData> playerData = new ConcurrentHashMap<>();

    public void createData(Player player) {
        playerData.put(player.getUniqueId(), new PlayerData(player));
    }

    public PlayerData getData(Player player) {
        return playerData.get(player.getUniqueId());
    }

    public void removeData(Player player) {
        playerData.remove(player.getUniqueId());
    }
}
