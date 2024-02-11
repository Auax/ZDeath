package org.zlls.zdeath.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.zlls.zdeath.ZDeath;
import org.zlls.zdeath.misc.GetLocation;

import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;

public class Storm {
    private final ZDeath plugin; // Store a reference to the plugin instance
    private final int maxSize;
    private final int minSize;
    private final int rate;
    private int currentSize;
    private final World world;
    private boolean running = false;


    public Storm(ZDeath plugin) {
        this.plugin = plugin;
        this.world = Bukkit.getWorld(Objects.requireNonNull(plugin.CONFIG.getString("world")));

        // Get values from config
        minSize = plugin.CONFIG.getInt("min_storm_size");
        rate = plugin.CONFIG.getInt("storm_rate");
        maxSize = plugin.CONFIG.getInt("max_storm_size");

        // Set worldBorder center
        Location location = GetLocation.GetConfigLocation(plugin);
        world.getWorldBorder().setCenter(location);

        // Set worldBorder size
        currentSize = maxSize;
        setSize(maxSize);
    }


    public void setSize(int size) {
        world.getWorldBorder().setSize(size);
    }

    public void start() {
        if (!running) {
            running = true;

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!running) {
                        getLogger().info("Stopped storm!");
                        this.cancel();
                    }
                    // Calculate new size
                    currentSize = currentSize - rate;
                    if (currentSize >= minSize) {
                        setSize(currentSize);
                    } else {
                        // Hit the min size limit
                        setSize(minSize);
                        running = false;
                        this.cancel(); // Stop the BukkitRunnable
                    }
                }

            }.runTaskTimer(plugin, 0, 20); // Run the task every second (20 ticks = 1 second)

        } else {
            getLogger().info("Storm already updating!");
        }
    }

    public void stop() {
        running = false;
    }

    public void reset() {
        stop();

        // Wait a second to avoid trouble with resetting the size
        Bukkit.getScheduler().runTaskLater(plugin, () -> setSize(maxSize), 20L); // 20L -> 1s

    }
}
