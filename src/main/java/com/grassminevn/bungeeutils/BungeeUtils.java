package com.grassminevn.bungeeutils;

import com.grassminevn.bungeeutils.command.CommandHandler;
import com.grassminevn.bungeeutils.command.LobbyCommand;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class BungeeUtils extends Plugin implements Listener {
    private Configuration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        getProxy().getPluginManager().registerListener(this, new EventManager(this));
        getProxy().getPluginManager().registerCommand(this, new CommandHandler(this));
        getProxy().getPluginManager().registerCommand(this, new LobbyCommand());
    }

    public Configuration getConfig() {
        return config;
    }

    public void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (final IOException e) {
            throw new RuntimeException("Unable to load configuration", e);
        }
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "config.yml"));
        }
        catch (final IOException e) {
            throw new RuntimeException("Unable to save configuration", e);
        }
    }

    private void saveDefaultConfig() {
        getDataFolder().mkdir();
        final File file = new File(getDataFolder(), "config.yml");
        if (file.exists()) return;
        try {
            Files.copy(getResourceAsStream("config.yml"), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) {
            throw new RuntimeException("Unable to create configuration file", e);
        }
    }
}
