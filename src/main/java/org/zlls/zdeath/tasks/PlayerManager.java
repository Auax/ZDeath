package org.zlls.zdeath.tasks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.zlls.zdeath.ZDeath;

import javax.annotation.Nullable;

public class PlayerManager {
    private final ZDeath plugin; // Store a reference to the plugin instance

    public static final String LIVES_KEY = "player_lives";
    public static final String KILLS_KEY = "player_kills";
    public static final String SCOREBOARD_KEY = "player_scoreboard";
    public static final String OBJECTIVE_KEY = "player_scoreboard_objective";
    private static Integer DEFAULT_LIVES = 3;

    public PlayerManager(ZDeath plugin) {
        this.plugin = plugin;

        DEFAULT_LIVES = plugin.CONFIG.getInt("lives");

        // Set default values for all players
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            resetAttributes(player);
        }
    }

    /**
     * Only reset safe attributes such as kills and lives
     */
    public void resetAttributes(Player player) {
        setValue(player, DEFAULT_LIVES, LIVES_KEY);
        setValue(player, 0, KILLS_KEY);
    }

    public void registerScoreboard(Player player, Scoreboard scoreboard, Objective objective) {
        setValue(player, scoreboard, SCOREBOARD_KEY);
        setValue(player, objective, OBJECTIVE_KEY);
    }

    public Object getValue(Player player, String key) {
        if (player.hasMetadata(key)) {
            for (MetadataValue value : player.getMetadata(key)) {
                return value.value();
            }
        }
        return null;
    }

    public void setValue(Player player, @Nullable Object value, String key) {
        player.setMetadata(key, new FixedMetadataValue(plugin, value));
    }

    public int getIntFromKey(Player player, String key) {
        if (player.hasMetadata(key)) {
            for (MetadataValue value : player.getMetadata(key)) {
                Object v = value.value();
                if (v instanceof Integer) {
                    return (int) v;
                }
            }
        }
        return DEFAULT_LIVES;
    }

    public void decreaseLives(Player player) {
        int currentLives = getIntFromKey(player, LIVES_KEY);
        setValue(player, currentLives - 1, LIVES_KEY);

        if (currentLives - 1 <= 0) {
            // One less player remaining
            plugin.remainingPlayers = plugin.remainingPlayers - 1;
            // Player eliminated, must spectate now
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    public void addKill(Player player) {
        int currentKills = getIntFromKey(player, KILLS_KEY);
        setValue(player, currentKills + 1, KILLS_KEY);
        // Handle logic for the kill

    }
}
