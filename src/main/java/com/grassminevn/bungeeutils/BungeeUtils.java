package com.grassminevn.bungeeutils;

import com.grassminevn.bungeeutils.command.LobbyCommand;
import com.grassminevn.bungeeutils.command.MainCommand;
import com.grassminevn.bungeeutils.config.ConfigManager;
import com.grassminevn.bungeeutils.listener.DisconnectListener;
import com.grassminevn.bungeeutils.listener.PingListener;
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
        if (configManager.getConfig().getBoolean("disconnect.enabled")) {
            getProxy().getPluginManager().registerListener(this, new DisconnectListener(this));
        }
        if (configManager.getConfig().getBoolean("modify-server-info.enabled")) {
            getProxy().getPluginManager().registerListener(this, new PingListener(this));
        }
        if (configManager.getConfig().getBoolean("lobby-command.enabled")) {
            getProxy().getPluginManager().registerCommand(this, new LobbyCommand(this));
        }
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
