package me.matthewe.challenge.commands;

import me.matthewe.challenge.GodComplexChallenge;
import me.matthewe.challenge.command.Command;
import me.matthewe.challenge.command.SpigotCommand;
import me.matthewe.challenge.configs.MessageConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by Matthew E on 2/3/2018.
 */
@Command(name = "godcomplexreload", usage = "/GodComplexChallengeReload", aliases = "reloadconfig")
public class GodComplexChallengeReloadCommand extends SpigotCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(MessageConfig.permissions_reloadConfig)) {
            GodComplexChallenge.getConfigManager().reload();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_reloadConfig));
        }
    }
}
