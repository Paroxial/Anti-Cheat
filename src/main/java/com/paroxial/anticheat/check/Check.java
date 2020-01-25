package com.paroxial.anticheat.check;

import com.paroxial.anticheat.Theme;
import com.paroxial.anticheat.check.event.CheckBanEvent;
import com.paroxial.anticheat.check.event.CheckFlagEvent;
import com.paroxial.anticheat.packet.PacketDirection;
import com.paroxial.anticheat.packet.handler.PacketHandler;
import com.paroxial.anticheat.player.PlayerData;
import com.paroxial.anticheat.utils.ReflectionUtils;
import com.paroxial.anticheat.utils.TimeUtils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Data
public abstract class Check implements PacketHandler {
    protected final String name;
    protected final PlayerData data;

    private final Set<PacketDirection> directions = new HashSet<>();

    protected Set<Long> violationTimes = new HashSet<>();
    protected long violationDecay = 5000L; // in milliseconds

    protected int violations;
    protected int maxViolations = -1;

    protected long lastFlagTime;

    public Check(String name, int maxViolations, PlayerData data, PacketDirection... direction) {
        this.name = name;
        this.data = data;
        this.maxViolations = maxViolations;
        this.directions.addAll(Arrays.asList(direction));
    }

    protected void trigger(String... arguments) {
        this.trigger(1, arguments);
    }

    protected void trigger(int violationAmount, String... arguments) {
        addViolations(violationAmount);
        alert(arguments);

        if (maxViolations == -1) {
            return;
        }

        boolean shouldBan = violations >= maxViolations && !data.getPlayerBanned().get();

        if (shouldBan) {
            CheckBanEvent event = new CheckBanEvent(this, "Anti-Cheat (" + name + ")");
            Bukkit.getPluginManager().callEvent(event);
        }

        lastFlagTime = TimeUtils.getMonotonicMillis();
    }

    protected void addViolations(int violationAmount) {
        long now = TimeUtils.getMonotonicMillis();

        if (violationDecay != -1) {
            violationTimes.removeIf(timeStamp -> now - timeStamp >= violationDecay);
        }

        violations = violationTimes.size() + violationAmount;

        for (int i = 0; i < violationAmount; i++) {
            violationTimes.add(now);
        }
    }

    protected void removeViolations(int violationAmount) {
        violations = Math.max(0, violations - violationAmount);
    }

    protected void incrementViolations() {
        violations++;
    }

    protected void decrementViolations() {
        violations = Math.max(0, violations - 1);
    }

    protected void clearViolations() {
        violations = 0;
        violationTimes.clear();
    }

    protected void alert(String... arguments) {
        Player player = data.getPlayer();
        StringJoiner joiner = new StringJoiner("] [");

        for (String argument : arguments) {
            joiner.add(argument);
        }

        String playerData = "[VL: " + violations + ", Ping: " + ReflectionUtils.getPing(player) + "]";
        String extraData = arguments.length == 0 ? "" : "[" + joiner.toString() + "]";
        String message = Theme.PRIMARY + player.getName() + Theme.SECONDARY + " triggered check " +
                Theme.PRIMARY + name + Theme.ACCENT + " " + playerData + " " + Theme.PRIMARY + extraData;

        boolean alerting = true; // TODO: determine whether or not to alert for this flag for specific players

        CheckFlagEvent event = new CheckFlagEvent(this, data, message, alerting);
        Bukkit.getPluginManager().callEvent(event);
    }
}
