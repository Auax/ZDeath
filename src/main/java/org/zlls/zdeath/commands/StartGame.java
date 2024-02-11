package org.zlls.zdeath.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.zlls.zdeath.ZDeath;
import org.zlls.zdeath.misc.GetLocation;
import org.zlls.zdeath.tasks.BarrierManager;
import org.zlls.zdeath.tasks.Countdown;
import org.zlls.zdeath.tasks.Storm;

import java.util.ArrayList;
import java.util.List;

public class StartGame implements CommandExecutor {
    private final ZDeath plugin; // Store a reference to the plugin instance

    public StartGame(ZDeath plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        // Sender is console
        if (!(commandSender instanceof Player)) {
            return true;
        }

        // Set global variable to true (important to the scoreboard works in game mode)
        plugin.isGameStarted = true;
        plugin.remainingPlayers = Bukkit.getServer().getOnlinePlayers().size();

        List<ItemStack> itemStacks = new ArrayList<>();
        ConfigurationSection kit = plugin.CONFIG.getConfigurationSection("kit");
        if (kit != null) {
            for (String item : kit.getKeys(false)) {
                Material material = Material.getMaterial(item);
                // If the material is null simply do nothing ;)
                if (material != null) {
                    int amount = kit.getInt(item);
                    ItemStack itemStack = new ItemStack(material, amount);
                    itemStacks.add(itemStack);
                }
            }
        }
        ItemStack[] itemsArray = itemStacks.toArray(new ItemStack[0]); // Convert the list to an array

        // Create location
        Location location = GetLocation.GetConfigLocation(plugin);

        // Create barrier to avoid players leaving the zone before the countdown is over
        BarrierManager.buildBarrier(location, Material.BARRIER, 2);

        for (Player playerOnline : Bukkit.getServer().getOnlinePlayers()) {
            // Reset kills & lives
            plugin.playerManager.resetAttributes(playerOnline);

            // Manage GameMode
            playerOnline.setGameMode(GameMode.SURVIVAL);
            // Reset player health, food...
            playerOnline.setLevel(0);
            playerOnline.setHealth(20);
            playerOnline.setFoodLevel(20);

            // Manage Inventory
            PlayerInventory inventory = playerOnline.getInventory();
            inventory.clear();

            playerOnline.getInventory().addItem(itemsArray);

            // Tp all players to location
            playerOnline.teleport(location);
        }


        // Initiate countdown
        Countdown countdown = new Countdown(plugin, 10, () -> {
            for (Player playerOnline : Bukkit.getServer().getOnlinePlayers()) {
                playerOnline.sendTitle(plugin.CONFIG.getString("game_start_title_s"), plugin.CONFIG.getString("game_start_title_s1"), 10, 100, 50);
            }
            BarrierManager.buildBarrier(location, Material.AIR, 2);
        });
        countdown.startCountdown();

        // Start storm update
        plugin.storm.start();

        return true;

    }
}
