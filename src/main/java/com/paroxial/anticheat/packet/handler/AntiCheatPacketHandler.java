package com.paroxial.anticheat.packet.handler;

import com.paroxial.anticheat.AntiCheatPlugin;
import com.paroxial.anticheat.packet.PacketDirection;
import com.paroxial.anticheat.packet.WrappedPacket;
import com.paroxial.anticheat.player.PlayerData;
import com.paroxial.anticheat.protocol.TinyProtocol;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;

public class AntiCheatPacketHandler extends TinyProtocol {
    private final AntiCheatPlugin plugin;

    public AntiCheatPacketHandler(AntiCheatPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
        if (sender != null) {
            // Process the packet on a non-Netty thread
            plugin.processTask(() -> handleInboundPacket(sender, packet));
        }

        return super.onPacketInAsync(sender, channel, packet);
    }

    @Override
    public Object onPacketOutAsync(Player receiver, Channel channel, Object packet) {
        if (receiver != null) {
            // Process the packet on a non-Netty thread
            plugin.processTask(() -> handleOutboundPacket(receiver, packet));
        }

        return super.onPacketOutAsync(receiver, channel, packet);
    }

    private void handleInboundPacket(Player player, Object packet) {
        PlayerData data = plugin.getPlayerDataManager().getData(player);

        if (data == null || data.getPlayerBanned().get()) {
            return;
        }

        WrappedPacket wrapper = new WrappedPacket(packet, PacketDirection.INBOUND);

        data.getStatsTracker().handlePacket(wrapper);

        data.getInboundChecks().forEach(check -> check.handlePacket(wrapper));
    }

    private void handleOutboundPacket(Player player, Object packet) {
        PlayerData data = plugin.getPlayerDataManager().getData(player);

        if (data == null || data.getPlayerBanned().get()) {
            return;
        }

        WrappedPacket wrapper = new WrappedPacket(packet, PacketDirection.OUTBOUND);

        data.getStatsTracker().handlePacket(wrapper);

        data.getOutboundChecks().forEach(check -> check.handlePacket(wrapper));
    }
}
