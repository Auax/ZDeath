package org.zlls.zdeath.misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.zlls.zdeath.ZDeath;

import java.util.List;
import java.util.Objects;

public class GetLocation {
    public static Location GetConfigLocation(ZDeath plugin) {
        World world = Bukkit.getWorld(Objects.requireNonNull(plugin.CONFIG.getString("world")));

        List<Double> coords = (List<Double>) plugin.CONFIG.getList("location");
        Location location;

        if (coords != null) {
            location = new Location(world, coords.get(0), coords.get(1), coords.get(2), 90, 0);
        } else {
            location = new Location(world, 0, 100, 0, 90, 0);
        }
        return location;
    }
}
