package me.matthewe.challenge.music;

import me.matthewe.challenge.utilities.BlockUtilities;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Created by Matthew E on 2/3/2018.
 */
public class MusicBlock {
    protected JsonLocation block;
    public static final IronMusicBlock IRON_MUSIC_BLOCK;
    public static final NetherMusicBlock NETHER_MUSIC_BLOCK;

    static {
        IRON_MUSIC_BLOCK = new IronMusicBlock(null, null, null);
        NETHER_MUSIC_BLOCK = new NetherMusicBlock(null, -1);
        System.out.println("Loaded music blocks");
    }

    public MusicBlock(JsonLocation block) {
        this.block = block;
    }

    public JsonLocation getBlock() {
        return block;
    }

    public Block getSign() {
        return BlockUtilities.getSignAttachedToBlock(block.toLocation().getBlock());
    }

    public void play(Player player) {
    }

    public Material getMaterial() {
        return null;
    }

    public String[] getSignText() {
        return new String[4];
    }
}
