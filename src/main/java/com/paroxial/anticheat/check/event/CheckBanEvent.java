package com.paroxial.anticheat.check.event;

import com.paroxial.anticheat.check.Check;
import lombok.Getter;

public class CheckBanEvent extends CheckEvent {

    @Getter
    private final String reason;

    public CheckBanEvent(Check check, String reason) {
        super(check);
        this.reason = reason;
    }
}
