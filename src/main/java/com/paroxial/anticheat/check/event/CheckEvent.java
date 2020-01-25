package com.paroxial.anticheat.check.event;

import com.paroxial.anticheat.check.Check;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class CheckEvent extends Event {
    private static final HandlerList handerList = new HandlerList();

    @Getter
    private final Check check;

    public CheckEvent(Check check) {
        this.check = check;
    }

    public static HandlerList getHandlerList() {
        return CheckEvent.handerList;
    }

    public HandlerList getHandlers() {
        return CheckEvent.handerList;
    }
}
