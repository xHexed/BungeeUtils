package com.mcgautruc.bungeecord;

import com.mcgautruc.bungeecord.command.CommandHandler;
import com.mcgautruc.bungeecord.command.LobbyCommand;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BungeeCord extends Plugin implements Listener {
    private Configuration config;
    private Favicon favicon;
    private final List<String> commands = new ArrayList<>();

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
        final File file = new File(getDataFolder(), "server-icon.png");
        if (file.exists())
            try {
                favicon = Favicon.create(ImageIO.read(file));
            }
            catch (final IOException e) {
                getLogger().warning("Favicon file is invalid or missing.");
            }
        commands.clear();
        config.getStringList("commands").forEach((c) -> commands.add(c.toLowerCase()));
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
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        try (final InputStream config = getResourceAsStream("config.yml");
            final BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(getDataFolder(), "config.yml")))) {
            stream.write(config.readAllBytes());
            stream.flush();
        } catch (final IOException e) {
            throw new RuntimeException("Unable to create configuration file", e);
        }
    }

    Favicon getFavicon() {
        return favicon;
    }

    List<String> getCommands() {
        return commands;
    }
}
