package org.zlls.zdeath.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.zlls.zdeath.ZDeath;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

public class GameScoreboard {
    private final ZDeath plugin; // Store a reference to the plugin instance
    private final ScoreboardManager manager = Bukkit.getScoreboardManager();


    public GameScoreboard(ZDeath plugin) {
        this.plugin = plugin;
    }

    public void init() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateScoreboard();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }


    public Scoreboard createScoreboard() {
        return Objects.requireNonNull(manager).getNewScoreboard();
    }

    public Objective createObjective(Scoreboard scoreboard) {
        Objective objective = scoreboard.registerNewObjective("ZDeath", Criteria.DUMMY, ChatColor.BLUE + ChatColor.BOLD.toString() + "ZDeath");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        return objective;
    }

    private void updateScoreboard() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            // Get player's scoreboard
            Scoreboard scoreboard = (Scoreboard) plugin.playerManager.getValue(player, PlayerManager.SCOREBOARD_KEY);
            scoreboard.getEntries().forEach(scoreboard::resetScores); // Clear existing scores
            // Get scoreboard's objective
            Objective objective = (Objective) plugin.playerManager.getValue(player, PlayerManager.OBJECTIVE_KEY);

            List<String> lines = new ArrayList<>();
            lines.add(ChatColor.STRIKETHROUGH + "-------------------");
            lines.add(ChatColor.RED + "» " + ChatColor.BLUE + "Ping: " + ChatColor.RED + player.getPing() + ChatColor.GRAY + " ms");

            if (plugin.isGameStarted) {
                lines.add(ChatColor.RED + "» " + ChatColor.BLUE + plugin.CONFIG.getString("sc_players_left") + ": " + ChatColor.RED + plugin.remainingPlayers);
                lines.add(ChatColor.RED + "» " + ChatColor.BLUE + plugin.CONFIG.getString("sc_lives") + ": " + ChatColor.RED + plugin.playerManager.getIntFromKey(player, PlayerManager.LIVES_KEY));
                lines.add(ChatColor.RED + "» " + ChatColor.BLUE + plugin.CONFIG.getString("sc_kills") + ": " + ChatColor.RED + plugin.playerManager.getIntFromKey(player, PlayerManager.KILLS_KEY));
            }


            int line = lines.size();

            for (String text : lines) {
                objective.getScore(text).setScore(line);
                line--;
            }

            player.setScoreboard(scoreboard);
        }
    }
}
