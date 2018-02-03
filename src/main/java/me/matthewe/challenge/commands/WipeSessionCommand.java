package me.matthewe.challenge.commands;

import me.matthewe.challenge.GodComplexChallenge;
import me.matthewe.challenge.command.Command;
import me.matthewe.challenge.command.SpigotCommand;
import me.matthewe.challenge.player.MusicPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Matthew E on 2/3/2018.
 */
@Command(name = "wipesession", usage = "/wipesession")
public class WipeSessionCommand extends SpigotCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            MusicPlayer musicPlayer = GodComplexChallenge.getMusicPlayers().getPlayer(player.getUniqueId());
            musicPlayer.setMusicSession(null);
            musicPlayer.setInMusicSession(false);
            player.sendMessage(ChatColor.RED+"Forced cleared music session");
        }
    }
}
