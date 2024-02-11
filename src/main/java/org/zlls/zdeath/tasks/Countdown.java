package org.zlls.zdeath.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Bukkit.getLogger;

public class Countdown {
    private final JavaPlugin plugin; // Store a reference to the plugin instance
    private int timeLeftInSeconds;
    private boolean running = false;
    private Runnable onFinishCallback;

    public Countdown(JavaPlugin plugin, int initialTimeInSeconds) {
        this.plugin = plugin;
        this.timeLeftInSeconds = initialTimeInSeconds;
    }

    public Countdown(JavaPlugin plugin, int initialTimeInSeconds, Runnable callback) {
        this.plugin = plugin;
        this.timeLeftInSeconds = initialTimeInSeconds;
        this.onFinishCallback = callback;
    }

    public void startCountdown() {
        if (!running) {
            running = true;

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (timeLeftInSeconds > 0) {
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                            player.sendTitle(ChatColor.BLUE + String.valueOf(timeLeftInSeconds), "", 10, 100, 50);
                        }
                        timeLeftInSeconds--; // Decrement time
                    } else {
                        // Countdown finished, perform any necessary actions
                        running = false;
                        this.cancel(); // Stop the BukkitRunnable

                        // Run callback method
                        if (onFinishCallback != null) {
                            onFinishCallback.run();
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 20); // Run the task every second (20 ticks = 1 second)

        } else {
            getLogger().info("Countdown already running!");
        }
    }
}
