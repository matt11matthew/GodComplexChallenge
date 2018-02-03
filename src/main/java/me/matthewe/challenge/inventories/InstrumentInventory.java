package me.matthewe.challenge.inventories;

import me.matthewe.challenge.configs.MessageConfig;
import me.matthewe.challenge.inventory.MeInventory;
import me.matthewe.challenge.inventory.item.ClickHandler;
import me.matthewe.challenge.inventory.item.MeInventoryItem;
import me.matthewe.challenge.music.IronMusicBlock;
import me.matthewe.challenge.music.MusicBlock;
import me.matthewe.challenge.player.MusicSession;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Matthew E on 2/3/2018.
 */
public class InstrumentInventory extends MeInventory {

    public InstrumentInventory(MusicSession musicSession) {
        super("Pick Instrument", 18);
        int slot = 0;
        for (Instrument instrument : Instrument.values()) {
            setItem(slot, new MeInventoryItem(new ItemStack(Material.NOTE_BLOCK))
                    .named("&aInstrument " + instrument.name() + " &7(Click)")
                    .lore("&7Click to set instrument")
                    .withClickHandler(new ClickHandler((player, itemStack, slot1) -> {
                        musicSession.setCurrentInstrument(instrument);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_setInstrument).replaceAll("%instrument%", instrument.toString()));
                        player.closeInventory();
                        List<MusicBlock> noteBlocks = musicSession.getNoteBlocks();
                        noteBlocks.add(new IronMusicBlock(musicSession.getCurrent(), musicSession.getCurrentTone(), musicSession.getCurrentInstrument()));
                        musicSession.setNoteBlocks(noteBlocks);
                        musicSession.setStage(MusicSession.Stage.WAITING_FOR_NEXT_ACTION);
                    }, ClickType.values())));
            slot++;
        }
    }
}
