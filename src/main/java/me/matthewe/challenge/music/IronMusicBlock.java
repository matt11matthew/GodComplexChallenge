package me.matthewe.challenge.music;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;

/**
 * Created by Matthew E on 2/3/2018.
 */
public class IronMusicBlock extends MusicBlock {
    private Note.Tone tone;
    private Instrument instrument;

    public IronMusicBlock(JsonLocation block, Note.Tone tone, Instrument instrument) {
        super(block);
        this.tone = tone;
        this.instrument = instrument;
    }


    public void play(Player player) {
        player.playNote(player.getLocation(), instrument, new Note(1, tone, true));
        Bukkit.getServer().broadcastMessage("Playing " + block.toString() + " " + tone.toString() + " " + instrument.toString() + getMaterial().toString());
    }


    public Material getMaterial() {
        return Material.IRON_BLOCK;
    }


    public String[] getSignText() {
        return new String[] {" ", tone.toString(), instrument.name()," "};
    }
}
