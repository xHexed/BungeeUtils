package com.grassminevn.bungeeutils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventManager implements Listener {
    private final ServerInfo lobby;
    private final BaseComponent[] disconnectHeaderMessage;
    private final BaseComponent[] disconnectFooterMessage;

    EventManager(final BungeeUtils plugin) {
        lobby  = ProxyServer.getInstance().getServerInfo(plugin.getConfigManager().getConfig().getString("lobby-server", "lobby"));
        disconnectHeaderMessage = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plugin.getConfigManager().getConfig().getString("messages.disconnect.header")));
        disconnectFooterMessage = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plugin.getConfigManager().getConfig().getString("messages.disconnect.footer")));
    }

    @EventHandler
    public void onPlayerDisconnect(final ServerKickEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final Server server = player.getServer();
        if (event.getKickedFrom() != lobby && server != null && !server.getInfo().equals(lobby)) {
            event.setCancelled(true);
            event.setCancelServer(lobby);
            player.sendMessage(disconnectHeaderMessage);
            for (final BaseComponent component : event.getKickReasonComponent()) {
                player.sendMessage(new TextComponent(TextComponent.fromLegacyText(
                        ChatColor.translateAlternateColorCodes('&', component.toLegacyText()))));
            }
            player.sendMessage(disconnectFooterMessage);
        }
    }
}
