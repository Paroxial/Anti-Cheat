package com.paroxial.anticheat.check.event;

import com.paroxial.anticheat.check.Check;
import com.paroxial.anticheat.player.PlayerData;
import lombok.Getter;

@Getter
public class CheckFlagEvent extends CheckEvent {
    private final PlayerData playerData;
    private final String message;
    private final boolean alerting;

    public CheckFlagEvent(Check check, PlayerData playerData, String alertMessage, boolean alerting) {
        super(check);
        this.playerData = playerData;
        this.message = alertMessage;
        this.alerting = alerting;
    }
}
