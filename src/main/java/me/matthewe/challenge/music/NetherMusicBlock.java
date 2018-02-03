package me.matthewe.challenge.music;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by Matthew E on 2/3/2018.
 */
public class NetherMusicBlock extends MusicBlock {
    private int ticks;

    public NetherMusicBlock(JsonLocation block, int ticks) {
        super(block);
        this.ticks = ticks;
    }

    @Override
    public void play(Player player) {
        Bukkit.getServer().broadcastMessage("Playing " + block.toString() + " " + ticks + " " + getMaterial().toString());
    }

    @Override
    public Material getMaterial() {
        return Material.NETHERRACK;
    }

    @Override
    public String[] getSignText() {
        return new String[]{" ", "Ticks", ticks + "", " "};
    }

    public int getTicks() {
        return ticks;
    }
}
