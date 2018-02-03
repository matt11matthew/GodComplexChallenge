package me.matthewe.challenge.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by Matthew E on 12/13/2017.
 */
public abstract class MeConfig {
    private final String name;
    private File file;

    public MeConfig(String name) {
        this.name = name;
        MeConfigManager configManager = MeConfigManager.getInstance();
        if (configManager != null) {
            if (!configManager.getPlugin().getDataFolder().exists()) {
                configManager.getPlugin().getDataFolder().mkdirs();
            }
            this.file = new File(configManager.getPlugin().getDataFolder() + File.separator + name + ".yml");
            if (file.exists()) {
                load();
                save();
            } else {
                save();

            }
        }
    }

    public static String getMessage(String message) {
        return  ChatColor.translateAlternateColorCodes('&', message);
    }

    public void reload() {
        load();
        save();
    }

    public void save() {
        boolean doesConfigExist = this.file.exists();
        if (!doesConfigExist) {
            try {
                this.file.createNewFile();
                doesConfigExist = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration fileConfiguration = (doesConfigExist) ? YamlConfiguration.loadConfiguration(this.file) : null;
        for (Field field : getClass().getFields()) {
            String configKey = field.getName().replaceAll("_", ".");
            if (doesConfigExist) {
                try {
                    fileConfiguration.set(configKey, field.get(field.getType()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
        if (doesConfigExist) {
            try {
                fileConfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void load() {
        boolean doesConfigExist = this.file.exists();
        FileConfiguration fileConfiguration = (doesConfigExist) ? YamlConfiguration.loadConfiguration(this.file) : null;
        for (Field field : getClass().getFields()) {
            String configKey = field.getName().replaceAll("_", ".");
            if (doesConfigExist && fileConfiguration.isSet(configKey)) {
                try {

                    field.set(field.getType(), fileConfiguration.get(configKey, field.get(field.getType())));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    /**
     * Getter for property 'file'.
     *
     * @return Value for property 'file'.
     */
    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }
}