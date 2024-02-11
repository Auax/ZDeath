package org.zlls.zdeath.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zlls.zdeath.ZDeath;

public class GameStatus implements CommandExecutor {
    private final ZDeath plugin; // Store a reference to the plugin instance

    public GameStatus(ZDeath plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        // Sender is console
        if (!(commandSender instanceof Player)) {
            return true;
        }

        String status = (plugin.isGameStarted) ? "playing" : "stopped";
        commandSender.sendMessage("Game status: " + (status));
        return false;
    }
}
