package me.matthewe.challenge.player;

import me.matthewe.challenge.configs.MessageConfig;
import me.matthewe.challenge.inventories.NoteInventory;
import me.matthewe.challenge.listeners.ChatListener;
import me.matthewe.challenge.music.JsonLocation;
import me.matthewe.challenge.music.MusicBlock;
import me.matthewe.challenge.music.NetherMusicBlock;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.material.Sign;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 2/3/2018.
 */
public class MusicSession {
    private List<MusicBlock> noteBlocks = new ArrayList<>();
    private String name;
    private boolean isDone;
    private Stage stage;
    private Note.Tone currentTone;
    private Instrument currentInstrument;
    private JsonLocation current;
    private int ticks;
    private JsonLocation startBlock;
    private BlockFace blockFace;
    private BlockFace signFace;
    private BlockFace secondSignFace;
    private int currentIndex = 0;
    private int currentWaitTicks = -1;



    public JsonLocation getCurrent() {
        return current;
    }

    public void setCurrentLocation(JsonLocation current) {
        this.current = current;
    }

    public MusicSession() {
        this.name = "Unset";
        this.isDone = false;
        this.stage = Stage.WAITING_FOR_NEXT_ACTION;
        this.currentInstrument = null;
        this.currentTone = null;
        this.ticks = -1;
        this.startBlock = null;
    }

    public JsonLocation getStartBlock() {
        return startBlock;
    }

    public void setStartBlock(JsonLocation startBlock) {
        this.startBlock = startBlock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Note.Tone getCurrentTone() {
        return currentTone;
    }

    public void setCurrentTone(Note.Tone currentTone) {
        this.currentTone = currentTone;
    }

    public Instrument getCurrentInstrument() {
        return currentInstrument;
    }

    public void setCurrentInstrument(Instrument currentInstrument) {
        this.currentInstrument = currentInstrument;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public List<MusicBlock> getNoteBlocks() {
        return noteBlocks;
    }

    public String getName() {
        return name;
    }

    private Block getCurrentBlock() {
        if (noteBlocks.isEmpty()) {
            return startBlock.toLocation().getBlock();
        }
        Block block = noteBlocks.get(noteBlocks.size() - 1).getBlock().toLocation().getBlock();
        if (block == null) {
            return startBlock.toLocation().getBlock();
        }
        return block;
    }

    public boolean canPlaceHere(Block block) {
        Location start = startBlock.toLocation();

        Block currentBlock = null;
        if (noteBlocks.isEmpty()) {
            currentBlock = new JsonLocation(start).toLocation().getBlock();
        } else {
            currentBlock = getCurrentBlock();
        }
        if (this.blockFace == null) {

            this.blockFace = startBlock.toLocation().getBlock().getFace(block);
        } else {

            try {
                BlockFace face = currentBlock.getFace(block);
                if (face == null) {
                    return false;
                }

                if (face != this.blockFace) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        List<Block> blocks = new ArrayList<>();
        for (int i = 1; i < noteBlocks.size() + 4; i++) {
            Block relative = currentBlock.getRelative(blockFace, i);
            if (relative.getType() != Material.AIR) {
                blocks.add(relative);
            }
        }
        return !blocks.isEmpty();
    }


    public void onBlockPlace(Player player, MusicPlayer musicPlayer, BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.GOLD_BLOCK && (stage == Stage.WAITING_FOR_GOLD)) {
            Block relative = block.getRelative(signFace);
            relative.setType(Material.WALL_SIGN);
            Sign sign = (Sign) relative.getState().getData();
            sign.setFacingDirection(secondSignFace);
            stage = Stage.WAITING_FOR_NEXT_ACTION;
            return;
        }
        if (!canPlaceHere(block)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot place a block here");
            return;
        }
        if (block.getType() == MusicBlock.IRON_MUSIC_BLOCK.getMaterial()) {
            stage = Stage.NOTE_PROMPT;
            current = new JsonLocation(event.getBlock());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_pickNote));
            new NoteInventory(this).open(player);
        } else if (block.getType() == MusicBlock.NETHER_MUSIC_BLOCK.getMaterial()) {
            stage = Stage.TICK_PROMPT;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_pickTicks));
            ChatListener.listenForChat(player, playerChatEvent -> {
                playerChatEvent.setCancelled(true);
                int ticks = 0;
                try {
                    ticks = Integer.parseInt(playerChatEvent.getMessage().trim());
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Please enter valid ticks");
                    return true;
                }
                if (ticks < 1) {
                    player.sendMessage(ChatColor.RED + "Please enter valid ticks");
                    return true;
                }
                player.sendMessage(ChatColor.GREEN + "Set ticks to " + ticks);
                setTicks(ticks);
                setStage(Stage.WAITING_FOR_NEXT_ACTION);

                noteBlocks.add(new NetherMusicBlock(new JsonLocation(event.getBlock()), ticks));
                return false;
            });
            musicPlayer.setMusicSession(this);
        } else {
            event.setCancelled(true);
        }
    }

    public boolean isDone() {
        return isDone;
    }

    public void setNoteBlocks(List<MusicBlock> noteBlocks) {
        this.noteBlocks = noteBlocks;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void tick(Player player) {
        if (currentWaitTicks != -1 && (currentWaitTicks != -2) && (currentWaitTicks > 0)) {
            currentWaitTicks--;
            return;
        }
        MusicBlock musicBlock = noteBlocks.get(currentIndex);
        if (musicBlock instanceof NetherMusicBlock) {
            NetherMusicBlock netherMusicBlock = (NetherMusicBlock) musicBlock;
            currentWaitTicks = netherMusicBlock.getTicks();


        }
        musicBlock.play(player);
        currentIndex++;
        if (currentIndex >= noteBlocks.size()) {
            currentIndex = -1;
        }

    }

    public void setCurrentIndex(int i) {
        this.currentIndex = i;
    }

    public void finish(Player player) {
        player.sendMessage(ChatColor.GREEN + "Please type name");
        stage = Stage.TYPING_NAME;
        ChatListener.listenForChat(player, new ChatListener.OnChat() {
            @Override
            public boolean onChat(PlayerChatEvent event) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.GREEN + "Set name to " + event.getMessage());
                name = event.getMessage();
                stage = Stage.FINISHED;
                player.sendMessage(ChatColor.GREEN + "Finished");
                isDone = true;
                return false;
            }
        });
    }

    public void setSignFace(BlockFace signFace) {
        this.signFace = signFace;
    }

    public void setSecondSignFace(BlockFace secondSignFace) {
        this.secondSignFace = secondSignFace;
    }

    public static enum Stage {
        WAITING_FOR_NEXT_ACTION, NOTE_PROMPT, TICK_PROMPT, TYPING_NAME, FINISHED, WAITING_FOR_GOLD,
    }

}
