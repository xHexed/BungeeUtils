package com.grassminevn.bungeeutils.config;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigManager {
    private Configuration config;
    private final Plugin plugin;
    private final File configFile;

    public ConfigManager(final Plugin plugin) {
        this.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), "config.yml");
    }

    public Configuration getConfig() {
        return config;
    }

    public void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (final IOException e) {
            throw new RuntimeException("Unable to load configuration", e);
        }
    }

    public void saveDefaultConfig() {
        plugin.getDataFolder().mkdir();
        if (configFile.exists()) return;
        try {
            Files.copy(plugin.getResourceAsStream("config.yml"), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) {
            throw new RuntimeException("Unable to create configuration file", e);
        }
    }
}
