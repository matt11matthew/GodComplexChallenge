package me.matthewe.challenge.player;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Matthew E on 2/3/2018.
 */
public class MusicPlayers {
    private Map<UUID, MusicPlayer> players;

    public MusicPlayers(Map<UUID, MusicPlayer> players) {
        this.players = players;
    }

    public void loadPlayer(UUID uniqueId) {
        if (!players.containsKey(uniqueId)) {
            players.put(uniqueId, new MusicPlayer(uniqueId, false, false, null));
            System.out.println("Created player " + uniqueId.toString());
        }
        System.out.println("Loaded player " + uniqueId.toString());
    }

    public MusicPlayer getPlayer(UUID uniqueId) {
        return players.get(uniqueId);
    }
}
