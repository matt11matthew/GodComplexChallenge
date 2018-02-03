package me.matthewe.challenge;

import com.google.gson.Gson;
import me.matthewe.challenge.command.SpigotCommandHandler;
import me.matthewe.challenge.commands.GodComplexChallengeReloadCommand;
import me.matthewe.challenge.commands.MusicCommand;
import me.matthewe.challenge.commands.WipeSessionCommand;
import me.matthewe.challenge.config.MeConfigManager;
import me.matthewe.challenge.configs.MessageConfig;
import me.matthewe.challenge.inventory.MeInventoryClickListener;
import me.matthewe.challenge.inventory.MeInventoryCloseListener;
import me.matthewe.challenge.listeners.AsyncLoginListener;
import me.matthewe.challenge.listeners.BlockListeners;
import me.matthewe.challenge.listeners.ChatListener;
import me.matthewe.challenge.player.MusicPlayer;
import me.matthewe.challenge.player.MusicPlayers;
import me.matthewe.challenge.player.MusicSession;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 2/3/2018.
 */
public class GodComplexChallenge extends JavaPlugin {
    public static GodComplexChallenge INSTANCE;
    private static MeConfigManager configManager;
    private static MusicPlayers musicPlayers;
    private int musicTaskId;

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.loadConfig();
        this.registerListeners();
        this.registerCommands();
        this.loadPlayers();
        this.musicTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                MusicPlayer player1 = musicPlayers.getPlayer(player.getUniqueId());
                if (!player1.isInMusicSession()) {
                    continue;
                }
                MusicSession musicSession = player1.getMusicSession();
                if (musicSession.isDone() && (musicSession.getCurrentIndex() != -2)) {
                    musicSession.tick(player);
                }
                if (musicSession.getCurrentIndex() == -1) {
                    musicSession.setCurrentIndex(-2);
                    player.sendMessage("Type 1 to play again or 2 to cancel");
                    ChatListener.listenForChat(player, new ChatListener.OnChat() {
                        @Override
                        public boolean onChat(PlayerChatEvent event) {

                            event.setCancelled(true);
                            if (event.getMessage().equalsIgnoreCase("1")) {
                                musicSession.setCurrentIndex(0);
                                player.sendMessage("Playing");
                            } else {
                                player.sendMessage("Cancelled");
                                player1.setMusicSession(null);
                                player1.setInMusicSession(false);
                            }
                            return false;
                        }
                    });
                }
            }
        }, 1, 1);

    }

    @Override
    public void onDisable() {
        this.savePlayers();
        configManager.unload();
    }

    public static MusicPlayers getMusicPlayers() {
        return musicPlayers;
    }

    private void loadConfig() {
        configManager = new MeConfigManager(this);
        configManager.load(new MessageConfig());
    }

    private void registerCommands() {
        SpigotCommandHandler spigotCommandHandler = new SpigotCommandHandler();
        spigotCommandHandler.registerCommand(new GodComplexChallengeReloadCommand());
        spigotCommandHandler.registerCommand(new MusicCommand());
        spigotCommandHandler.registerCommand(new WipeSessionCommand());
        spigotCommandHandler.registerCommands();
    }

    private void savePlayers() {
        File file = new File(this.getDataFolder(), "players.json");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileUtils.writeStringToFile(file, new Gson().toJson(musicPlayers), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Saved players");
    }

    private void loadPlayers() {
        File file = new File(this.getDataFolder(), "players.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                musicPlayers = new MusicPlayers(new ConcurrentHashMap<>());
                FileUtils.writeStringToFile(file, new Gson().toJson(musicPlayers), Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Created players file");
        } else {
            try {
                musicPlayers = new Gson().fromJson(FileUtils.readFileToString(file, Charset.defaultCharset()), MusicPlayers.class);
                System.out.println("Loaded players");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static MeConfigManager getConfigManager() {
        return configManager;
    }

    private void registerListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new MeInventoryClickListener(), this);
        pluginManager.registerEvents(new MeInventoryCloseListener(), this);
        pluginManager.registerEvents(new AsyncLoginListener(), this);
        pluginManager.registerEvents(new BlockListeners(), this);
        pluginManager.registerEvents(new ChatListener(), this);
    }
}
