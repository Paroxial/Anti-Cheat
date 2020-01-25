package com.paroxial.anticheat;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class Theme {
    public final String PRIMARY = ChatColor.WHITE.toString();
    public final String SECONDARY = ChatColor.GRAY.toString();
    public final String ACCENT = ChatColor.YELLOW.toString();

    public static final String PREFIX = ACCENT + "[Anti-Cheat] " + SECONDARY;
}
