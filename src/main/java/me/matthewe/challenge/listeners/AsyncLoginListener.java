package me.matthewe.challenge.listeners;

import me.matthewe.challenge.GodComplexChallenge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 * Created by Matthew E on 2/3/2018.
 */
public class AsyncLoginListener implements Listener {
    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        GodComplexChallenge.getMusicPlayers().loadPlayer(event.getUniqueId());
    }
}
