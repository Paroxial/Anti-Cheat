package com.paroxial.anticheat;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.paroxial.anticheat.commands.BaseCommand;
import com.paroxial.anticheat.commands.impl.AntiCheatCommand;
import com.paroxial.anticheat.listeners.CheckListener;
import com.paroxial.anticheat.listeners.PlayerListener;
import com.paroxial.anticheat.managers.AlertManager;
import com.paroxial.anticheat.managers.PlayerDataManager;
import com.paroxial.anticheat.packet.handler.AntiCheatPacketHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiCheatPlugin extends JavaPlugin {

    @Getter
    private static AntiCheatPlugin instance;

    private ExecutorService processingThread;

    @Getter
    private PlayerDataManager playerDataManager;

    @Getter
    private AlertManager alertManager;

    @Getter
    @Setter
    private boolean banningEnabled = true;

    @Override
    public void onEnable() {
        instance = this;

        new AntiCheatPacketHandler(this);

        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("anticheat-processing-thread").build();
        processingThread = Executors.newSingleThreadExecutor(factory);
        playerDataManager = new PlayerDataManager();
        alertManager = new AlertManager();

        registerCommands(new AntiCheatCommand());
        registerListeners(new PlayerListener(), new CheckListener());
    }

    @Override
    public void onDisable() {
        processingThread.shutdown();
    }

    public void processTask(Runnable runnable) {
        if (!processingThread.isTerminated()) {
            processingThread.execute(runnable);
        }
    }

    private void registerCommands(BaseCommand... commands) {
        for (BaseCommand command : commands) {
            getCommand(command.getName()).setExecutor(command);
        }
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
