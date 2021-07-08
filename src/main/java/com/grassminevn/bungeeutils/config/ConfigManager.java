package com.grassminevn.bungeeutils.config;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

public class ConfigManager {
    private Configuration config;
    private Plugin plugin;
    private File configFile;

    public ServerInfo lobbyServer;
    public BaseComponent[] disconnectHeaderMessage;
    public BaseComponent[] disconnectFooterMessage;
    public Set<String> ignoreWords;
    public String serverType;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), "config.yml");
    }

    public Configuration getConfig() {
        return config;
    }

    public void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load configuration", e);
        }
        lobbyServer  = ProxyServer.getInstance().getServerInfo(config.getString("lobby-server", "lobby"));
        disconnectHeaderMessage = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', config.getString("messages.disconnect.header")));
        disconnectFooterMessage = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', config.getString("messages.disconnect.footer")));
        ignoreWords = new HashSet<>(config.getStringList("messages.ignore-words"));
        serverType = config.getString("server-type");
    }

    public void saveDefaultConfig() {
        plugin.getDataFolder().mkdir();
        if (configFile.exists()) return;
        try {
            Files.copy(plugin.getResourceAsStream("config.yml"), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create configuration file", e);
        }
    }
}
