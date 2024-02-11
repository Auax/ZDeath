package org.zlls.zdeath;

import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.zlls.zdeath.commands.GameStatus;
import org.zlls.zdeath.commands.StartGame;
import org.zlls.zdeath.commands.StopGame;
import org.zlls.zdeath.events.GameEvents;
import org.zlls.zdeath.tasks.GameScoreboard;
import org.zlls.zdeath.tasks.PlayerManager;
import org.zlls.zdeath.tasks.Storm;

import java.util.Objects;

public final class ZDeath extends JavaPlugin {

    public FileConfiguration CONFIG;
    public GameScoreboard scoreboard;
    public PlayerManager playerManager;
    public Storm storm;

    public boolean isGameStarted = false;
    public int remainingPlayers;

    @Override
    public void onEnable() {
        // Create config file
        saveDefaultConfig();
        // Retrieve config file
        CONFIG = this.getConfig();

        // Enable message
        getServer().getConsoleSender().sendMessage(Color.RED + "ZDEATH Plugin Enabled!");

        // Set lives to players
        playerManager = new PlayerManager(this);

        // Set storm manager
        storm = new Storm(this);

        // Start scoreboard
        scoreboard = new GameScoreboard(this);
        scoreboard.init();

        // Register events
        getServer().getPluginManager().registerEvents(new GameEvents(this), this);

        // Register commands
        // Start new game
        Objects.requireNonNull(this.getCommand("start-game")).setExecutor(new StartGame(this));
        // Force stop game
        Objects.requireNonNull(this.getCommand("stop-game")).setExecutor(new StopGame(this));
        // Game status
        Objects.requireNonNull(this.getCommand("game-status")).setExecutor(new GameStatus(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }
}
