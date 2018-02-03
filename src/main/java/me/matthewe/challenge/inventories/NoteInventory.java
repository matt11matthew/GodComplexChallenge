package me.matthewe.challenge.inventories;

import me.matthewe.challenge.configs.MessageConfig;
import me.matthewe.challenge.inventory.MeInventory;
import me.matthewe.challenge.inventory.item.ClickHandler;
import me.matthewe.challenge.inventory.item.MeInventoryItem;
import me.matthewe.challenge.inventory.item.OnClick;
import me.matthewe.challenge.player.MusicSession;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Matthew E on 2/3/2018.
 */
public class NoteInventory extends MeInventory {

    public NoteInventory(MusicSession musicSession) {
        super("Pick Note", 18);
        int slot = 0;
        for (Note.Tone tone : Note.Tone.values()) {
            setItem(slot, new MeInventoryItem(new ItemStack(Material.NOTE_BLOCK)).named("&aNote " + tone.toString() + " &7(Click)").lore("&7Click to set note").withClickHandler(new ClickHandler(new OnClick() {
                @Override
                public void onClick(Player player, ItemStack itemStack, int slot) {
                    musicSession.setCurrentTone(tone);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_setTone).replaceAll("%tone%", tone.toString()));
                    player.closeInventory();
                    musicSession.setStage(MusicSession.Stage.TICK_PROMPT);

                    new InstrumentInventory(musicSession).open(player);

                }
            }, ClickType.values())));
            slot++;
        }
    }
}
