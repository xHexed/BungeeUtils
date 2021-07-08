package com.grassminevn.bungeeutils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventManager implements Listener {
    private BungeeUtils plugin;

    public EventManager(BungeeUtils plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDisconnect(ServerKickEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Server server = player.getServer();
        for (BaseComponent component : event.getKickReasonComponent()) {
            if (plugin.getConfigManager().ignoreWords.contains(component.toLegacyText())) {
                return;
            }
        }
        if (event.getKickedFrom() != plugin.getConfigManager().lobbyServer && server != null && !server.getInfo().equals(plugin.getConfigManager().lobbyServer)) {
            event.setCancelled(true);
            event.setCancelServer(plugin.getConfigManager().lobbyServer);
            player.sendMessage(plugin.getConfigManager().disconnectHeaderMessage);
            for (BaseComponent component : event.getKickReasonComponent()) {
                player.sendMessage(new TextComponent(TextComponent.fromLegacyText(
                        ChatColor.translateAlternateColorCodes('&', component.toLegacyText()))));
            }
            player.sendMessage(plugin.getConfigManager().disconnectFooterMessage);
        }
    }

    @EventHandler(priority = 64)
    public void onProxyPing(ProxyPingEvent e) {
        if (e.getResponse() == null)
            return;
        ServerPing ping = e.getResponse();
        ping.getModinfo().setType(plugin.getConfigManager().serverType);
        e.setResponse(ping);
    }
}
