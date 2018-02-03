package me.matthewe.challenge.player;

import java.util.UUID;

/**
 * Created by Matthew E on 2/3/2018.
 */
public class MusicPlayer {
    private UUID uuid;
    private boolean music;
    private boolean inMusicSession;
    private MusicSession musicSession;

    public MusicPlayer(UUID uuid, boolean music, boolean inMusicSession, MusicSession musicSession) {
        this.uuid = uuid;
        this.music = music;
        this.inMusicSession = inMusicSession;
        this.musicSession = musicSession;
    }

    public MusicSession startMusicSession() {
        if (this.music && (!this.inMusicSession)) {
            this.musicSession = new MusicSession();
            this.inMusicSession = true;
            return musicSession;
        }
        return null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isMusic() {
        return music;
    }

    public boolean toggleMusic() {
        this.music = !music;
        return music;
    }

    public MusicSession getMusicSession() {
        return musicSession;
    }

    public boolean isInMusicSession() {
        return inMusicSession;
    }

    public void setMusicSession(MusicSession musicSession) {
        this.musicSession = musicSession;
    }

    public void setInMusicSession(boolean inMusicSession) {
        this.inMusicSession = inMusicSession;
    }
}
