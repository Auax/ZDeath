package org.zlls.zdeath.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zlls.zdeath.ZDeath;
import org.zlls.zdeath.misc.GetLocation;
import org.zlls.zdeath.tasks.PlayerManager;
import org.zlls.zdeath.tasks.Storm;

public class StopGame implements CommandExecutor {
    private final ZDeath plugin; // Store a reference to the plugin instance

    public StopGame(ZDeath plugin) {
        this.plugin = plugin;
    }


    public static void StopCurrentGame(ZDeath plugin, boolean forced) {
        // We don't want to look for a winner when forcing the game to stop
        Player winner = null;
        if (!(forced)) {
            for (Player playerOnline : Bukkit.getServer().getOnlinePlayers()) {
                if (plugin.playerManager.getIntFromKey(playerOnline, PlayerManager.LIVES_KEY) > 0) {
                    winner = playerOnline;
                }
            }
        }

        // Display win title
        for (Player playerOnline : Bukkit.getServer().getOnlinePlayers()) {
            if (winner != null)
                playerOnline.sendTitle(plugin.CONFIG.getString("game_end_title_s"), plugin.CONFIG.getString("game_end_title_s1") + " " + winner.getName() + "!", 10, 100, 50);
            playerOnline.setGameMode(GameMode.SURVIVAL);
        }

        // Reset storm
        plugin.storm.reset();

        // Force game to end
        plugin.isGameStarted = false;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(plugin.isGameStarted)) {
            commandSender.sendMessage("There is no game started!");
            return true;
        }

        for (Player playerOnline : Bukkit.getServer().getOnlinePlayers()) {
            playerOnline.sendTitle("Stopped", "game!", 10, 100, 50);
        }

        StopCurrentGame(plugin, true);


        return true;

    }
}
