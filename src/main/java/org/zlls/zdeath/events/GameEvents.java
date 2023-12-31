package org.zlls.zdeath.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.zlls.zdeath.ZDeath;
import org.zlls.zdeath.commands.StopGame;
import org.zlls.zdeath.tasks.PlayerManager;

public class GameEvents implements Listener {
    public ZDeath plugin;

    public GameEvents(ZDeath plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Register scoreboard (allows different scoreboards for every player)
        Scoreboard scoreboard = plugin.scoreboard.createScoreboard();
        Objective objective = plugin.scoreboard.createObjective(scoreboard);
        plugin.playerManager.registerScoreboard(player, scoreboard, objective);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Game must be started otherwise do nothing
        if (!(plugin.hasGameStarted)) {
            return;
        }

        // Remove a life from the player
        Player player = event.getEntity();
        plugin.playerManager.decreaseLives(player);

        if (plugin.remainingPlayers <= 1) {
            StopGame.StopCurrentGame(plugin, false);
        }

        // Handle kill (only if it's a player)
        EntityDamageEvent lastDamage = player.getLastDamageCause();
        if (lastDamage instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) lastDamage;
            if (entityEvent.getDamager() instanceof Player) {
                Player killer = (Player) entityEvent.getDamager();
                // Add one kill to the player that killed
                plugin.playerManager.addKill(killer);
            }
        }
    }
}
