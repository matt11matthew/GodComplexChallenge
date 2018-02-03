package me.matthewe.challenge.music;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Created by Matthew E on 2/3/2018.
 */
public class JsonLocation {
    private String world;
    private int x, y, z;

    public JsonLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return world + "," + x + "," + y + "," + z;
    }

    public JsonLocation(Location location) {
        this(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public JsonLocation(Block block) {
        this(block.getLocation());
    }


    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
