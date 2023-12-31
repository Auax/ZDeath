package org.zlls.zdeath.tasks;

import org.bukkit.Location;
import org.bukkit.Material;

public class BarrierManager {
    /**
     * Create a 1 block high barrier around the specified location.
     *
     * @param location  Where the barrier should be built.
     * @param blockType What Material to use.
     * @param radius    The radius of blocks, being the 'location' the center.
     */
    public static void buildBarrier(Location location, Material blockType, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (Math.abs(x) == radius || Math.abs(z) == radius) { // Only place blocks at the border
                    Location blockLocation = location.clone().add(x, 0, z); // Level with the player's head
                    blockLocation.getBlock().setType(blockType);
                }
            }
        }
    }
}
