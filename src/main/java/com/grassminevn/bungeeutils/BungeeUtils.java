package com.grassminevn.bungeeutils;

import com.grassminevn.bungeeutils.command.LobbyCommand;
import com.grassminevn.bungeeutils.command.MainCommand;
import com.grassminevn.bungeeutils.config.ConfigManager;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeUtils extends Plugin implements Listener {
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        loadPlugin();
    }

    public void loadPlugin() {
        configManager.saveDefaultConfig();
        configManager.reloadConfig();
        getProxy().getPluginManager().registerListener(this, new EventManager(this));
        getProxy().getPluginManager().registerCommand(this, new LobbyCommand());
        getProxy().getPluginManager().registerCommand(this, new MainCommand(this));
    }

    public void unloadPlugin() {
        getProxy().getPluginManager().unregisterCommands(this);
        getProxy().getPluginManager().unregisterListeners(this);
    }

    public void reloadPlugin() {
        unloadPlugin();
        loadPlugin();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
