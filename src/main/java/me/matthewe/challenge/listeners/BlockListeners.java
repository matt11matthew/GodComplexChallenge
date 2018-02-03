package me.matthewe.challenge.listeners;

import me.matthewe.challenge.GodComplexChallenge;
import me.matthewe.challenge.configs.MessageConfig;
import me.matthewe.challenge.music.JsonLocation;
import me.matthewe.challenge.music.MusicBlock;
import me.matthewe.challenge.player.MusicPlayer;
import me.matthewe.challenge.player.MusicPlayers;
import me.matthewe.challenge.player.MusicSession;
import me.matthewe.challenge.utilities.BlockUtilities;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Sign;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Matthew E on 2/3/2018.
 */
public class BlockListeners implements Listener {
    private List<UUID> waitingToPlaceSign = new ArrayList<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        MusicPlayers musicPlayers = GodComplexChallenge.getMusicPlayers();
        MusicPlayer player = musicPlayers.getPlayer(event.getPlayer().getUniqueId());
        Material type = event.getBlock().getType();


        if (waitingToPlaceSign.contains(player.getUuid())) {
            return;
        }
        if (player.isInMusicSession()) {
            List<MusicBlock> toRemoveMusicBlocks = new ArrayList<>();
            MusicSession musicSession = player.getMusicSession();
            boolean isDelete = false;
            List<MusicBlock> noteBlocks = musicSession.getNoteBlocks();
            toRemoveMusicBlocks.addAll(noteBlocks);
            if ((musicSession.getStartBlock().toLocation().equals(event.getBlock().getLocation()))) {
                isDelete = true;
            }
            if (!toRemoveMusicBlocks.isEmpty()) {

                noteBlocks.removeAll(toRemoveMusicBlocks);
                musicSession.setNoteBlocks(noteBlocks);
                event.getPlayer().sendMessage(ChatColor.RED + "Broke music block");
                for (MusicBlock toRemoveMusicBlock : toRemoveMusicBlocks) {
                    Block block = toRemoveMusicBlock.getBlock().toLocation().getBlock();
                    Block sign = BlockUtilities.getSignAttachedToBlock(block);
                    if (sign != null) {
                        sign.setType(Material.AIR);
                    }
                    block.setType(Material.AIR);
                    player.setMusicSession(null);
                    player.setInMusicSession(false);
                }
            }
            if (isDelete) {
                event.getPlayer().sendMessage(ChatColor.RED + "Cancelled.");
                for (MusicBlock musicBlock : musicSession.getNoteBlocks()) {
                    Block sign = musicBlock.getSign();
                    if (sign != null) {
                        sign.setType(Material.AIR);
                    }
                    musicBlock.getBlock().toLocation().getBlock().setType(Material.AIR);
                }
                Block block = musicSession.getStartBlock().toLocation().getBlock();
                Block sign = BlockUtilities.getSignAttachedToBlock(block);
                if (sign != null) {
                    sign.setType(Material.AIR);
                }
                block.setType(Material.AIR);
                player.setMusicSession(null);
                player.setInMusicSession(false);
            }

        }
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        MusicPlayers musicPlayers = GodComplexChallenge.getMusicPlayers();
        MusicPlayer player = musicPlayers.getPlayer(event.getPlayer().getUniqueId());
        Block block = event.getBlock();
        Material type = block.getType();
        if (player.isMusic()) {
            if (waitingToPlaceSign.contains(player.getUuid())) {
                if(!block.getType().toString().contains("SIGN")){
                    event.getPlayer().sendMessage(ChatColor.RED + "Please place sign on diamond block");


                }
                return;
            }
            if (player.isInMusicSession()&&(type.toString().contains("SIGN"))) {
                event.getPlayer().sendMessage(ChatColor.RED + "Playing");
                player.getMusicSession().finish(event.getPlayer());
                return;
            }
            if ((!player.isInMusicSession()) && (type == Material.DIAMOND_BLOCK)) {
                waitingToPlaceSign.add(player.getUuid());
                event.getPlayer().sendMessage(ChatColor.RED + "Please place sign on diamond block");
            } else if (player.isInMusicSession() && ((type == MusicBlock.IRON_MUSIC_BLOCK.getMaterial() || type == MusicBlock.NETHER_MUSIC_BLOCK.getMaterial() || type == Material.GOLD_BLOCK))) {
                player.getMusicSession().onBlockPlace(event.getPlayer(), player, event);

            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Block sign = event.getBlock();
        Player player = event.getPlayer();
        Block blockBehindSign = BlockUtilities.getBlockBehindSign(sign);
        MusicPlayers musicPlayers = GodComplexChallenge.getMusicPlayers();
        MusicPlayer musicPlayer = musicPlayers.getPlayer(event.getPlayer().getUniqueId());
        if (musicPlayer.isMusic() && (!musicPlayer.isInMusicSession()) && waitingToPlaceSign.contains(player.getUniqueId()) && (blockBehindSign != null) && (blockBehindSign.getType() == Material.DIAMOND_BLOCK)) {
            waitingToPlaceSign.remove(player.getUniqueId());
            musicPlayer.startMusicSession();
            MusicSession musicSession = musicPlayer.getMusicSession();
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_startMusicSession));
            musicSession.setStartBlock(new JsonLocation(blockBehindSign));
            Sign sign2 = (Sign) sign.getState().getData();
            musicSession.setSignFace(sign2.getFacing());
            musicSession.setSecondSignFace(sign2.getAttachedFace());
            musicPlayer.setInMusicSession(true);
            musicSession.setStage(MusicSession.Stage.WAITING_FOR_GOLD);


        }
    }

}

