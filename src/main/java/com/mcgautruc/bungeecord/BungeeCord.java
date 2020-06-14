package com.mcgautruc.bungeecord;

import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BungeeCord extends Plugin implements Listener {
    private Configuration config;
    private Favicon favicon;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this, new CommandHandler(this));
    }

    Configuration getConfig() {
        return config;
    }

    void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (final IOException e) {
            throw new RuntimeException("Unable to load configuration", e);
        }
        final File file = new File(getDataFolder(), "server-icon.png");
        if (file.exists())
            try {
                favicon = Favicon.create(ImageIO.read(file));
            }
            catch (final IOException e) {
                getLogger().warning("Favicon file is invalid or missing.");
            }
    }

    void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "config.yml"));
        }
        catch (final IOException e) {
            throw new RuntimeException("Unable to save configuration", e);
        }
    }

    private void saveDefaultConfig() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        final File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists())
            try {
                configFile.createNewFile();
            }
            catch (final IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
    }

    @EventHandler(priority = 64)
    public void onServerListPing(final ProxyPingEvent event) {
        if (config.getString("motd") != null) {
            final ServerPing ping = event.getResponse();
            ping.setDescriptionComponent(new TextComponent(TextComponent.fromLegacyText(config.getString("motd"))));
            if (favicon != null)
                ping.setFavicon(favicon);
            event.setResponse(ping);
        }
    }
}
