package me.matthewe.challenge.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthew E on 12/29/2017.
 */
public class ChatListener implements Listener {
    private static Map<Player, OnChat> chatListenMap;

    public ChatListener() {
        chatListenMap =new HashMap<>();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (chatListenMap.containsKey(event.getPlayer())) {
            chatListenMap.remove(event.getPlayer());
        }
    }

    public static void listenForChat(Player player, OnChat onChat) {
        if (chatListenMap.containsKey(player)) {
            chatListenMap.remove(player);
        }
        if (onChat != null) {
            chatListenMap.put(player, onChat);
        }
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (chatListenMap.containsKey(player)) {
            OnChat messageListener = chatListenMap.get(player);
            if (messageListener != null) {
                boolean b = messageListener.onChat(event);
                if (!b) {
                    chatListenMap.remove(player);
                }
            }
        }
    }

    @FunctionalInterface
    public static interface OnChat {
        boolean onChat(PlayerChatEvent event);
    }
}
