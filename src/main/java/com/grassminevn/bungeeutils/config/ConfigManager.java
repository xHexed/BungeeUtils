package com.grassminevn.bungeeutils.config;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigManager {
    private Configuration config;
    private Plugin plugin;
    private File configFile;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), "config.yml");
    }

    public Configuration getConfig() {
        return config;
    }

    public void reloadConfig() {
        ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
        try {
            config = provider.load(configFile);
            provider.save(config, configFile);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load configuration", e);
        }
    }

    public void saveDefaultConfig() {
        plugin.getDataFolder().mkdir();
        if (configFile.exists()) return;
        try {
            Files.copy(plugin.getResourceAsStream("config.yml"), configFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Unable to create configuration file", e);
        }
    }
}
