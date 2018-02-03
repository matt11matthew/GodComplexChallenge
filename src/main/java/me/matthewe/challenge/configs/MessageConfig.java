package me.matthewe.challenge.configs;

import me.matthewe.challenge.config.MeConfig;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class MessageConfig extends MeConfig {
    public static String permissions_reloadConfig = "godcomplex.reloadconfig";
    public static String messages_reloadConfig = "&aReloaded config";
    public static String messages_enableMusic="&aMusic Enabled";
    public static String messages_disableMusic="&cMusic Disabled";
    public static String messages_setTone="&aSet note to %tone%";
    public static String messages_setInstrument="&aSet instrument to %instrument%";
    public static String messages_pickNote="&aPlease pick a note.";
    public static String messages_pickTicks="&aPlease pick ticks.";
    public static String messages_startMusicSession="&aStarted music creation session";

    public MessageConfig() {
        super("messages");
    }
}