package me.matthewe.challenge.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthew E on 12/13/2017.
 */
public class MeConfigManager {
    private Map<String, MeConfig> meConfigMap;
    private JavaPlugin plugin;
    private static MeConfigManager instance;

    public MeConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;
        this.meConfigMap = new HashMap<>();
    }

    /**
     * Getter for property 'instance'.
     *
     * @return Value for property 'instance'.
     */
    public static MeConfigManager getInstance() {
        return instance;
    }

    public void load(MeConfig... meConfigs) {
        Arrays.asList(meConfigs).forEach(this::load);
    }


    private void load(MeConfig meConfig) {
        if (!this.meConfigMap.containsKey(meConfig.getName())) {
            this.meConfigMap.put(meConfig.getName(), meConfig);
        }
    }

    /**
     * Getter for property 'plugin'.
     *
     * @return Value for property 'plugin'.
     */
    public JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Getter for property 'meConfigMap'.
     *
     * @return Value for property 'meConfigMap'.
     */
    public Map<String, MeConfig> getMeConfigMap() {
        return meConfigMap;
    }

    public void unload() {
        reload();
    }

    public void reload() {
        this.meConfigMap.values().forEach(MeConfig::reload);
    }


}
