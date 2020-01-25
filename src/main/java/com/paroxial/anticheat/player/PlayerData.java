package com.paroxial.anticheat.player;

import com.paroxial.anticheat.check.Check;
import com.paroxial.anticheat.check.checks.combat.autoclicker.FastSwing;
import com.paroxial.anticheat.check.checks.combat.autoclicker.InvalidDig;
import com.paroxial.anticheat.check.checks.combat.autoclicker.SwingDeviation;
import com.paroxial.anticheat.check.checks.combat.killaura.FastAttack;
import com.paroxial.anticheat.check.checks.combat.killaura.NoSwing;
import com.paroxial.anticheat.check.checks.invalid.InvalidFlyingFrequency;
import com.paroxial.anticheat.check.checks.invalid.InvalidPitch;
import com.paroxial.anticheat.check.checks.invalid.InvalidSneak;
import com.paroxial.anticheat.check.checks.invalid.InvalidSprint;
import com.paroxial.anticheat.check.checks.movement.impl.Flight;
import com.paroxial.anticheat.packet.PacketDirection;
import com.paroxial.anticheat.player.packet.PlayerStatsTracker;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
public class PlayerData {
    private final Set<Check> inboundChecks = new HashSet<>();
    private final Set<Check> outboundChecks = new HashSet<>();

    private final Player player;
    private final PlayerStats stats;

    private final PlayerStatsTracker statsTracker;

    @Setter
    private AtomicBoolean playerBanned = new AtomicBoolean();

    public PlayerData(Player player) {
        this.player = player;
        this.stats = new PlayerStats();
        this.statsTracker = new PlayerStatsTracker(this);

        initializeChecks();
    }

    private void addCheck(Check check) {
        Set<PacketDirection> directionList = check.getDirections();

        if (directionList.contains(PacketDirection.INBOUND)) {
            inboundChecks.add(check);
        }

        if (directionList.contains(PacketDirection.OUTBOUND)) {
            outboundChecks.add(check);
        }
    }

    private void addChecks(Check... checks) {
        for (Check check : checks) {
            addCheck(check);
        }
    }

    private void initializeChecks() {
        // Auto-clicker
        addChecks(new InvalidDig(this), new FastSwing(this), new SwingDeviation(this));

        // Kill Aura
        addChecks(new FastAttack(this), new NoSwing(this));

        // Invalid
        addChecks(new InvalidPitch(this), new InvalidSprint(this), new InvalidSneak(this),
                new InvalidFlyingFrequency(this));

        // Movement
        addChecks(new Flight(this));
    }
}
