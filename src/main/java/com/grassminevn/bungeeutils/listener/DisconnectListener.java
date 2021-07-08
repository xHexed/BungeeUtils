package com.grassminevn.bungeeutils.listener;

import com.grassminevn.bungeeutils.BungeeUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.HashSet;
import java.util.Set;

public class DisconnectListener implements Listener {
    private ServerInfo lobbyServer;
    private BaseComponent[] disconnectHeaderMessage;
    private BaseComponent[] disconnectFooterMessage;
    private Set<String> ignoredWords;

    public DisconnectListener(BungeeUtils plugin) {
        Configuration config = plugin.getConfigManager().getConfig();
        lobbyServer = ProxyServer.getInstance().getServerInfo(config.getString("disconnect-server"));
        disconnectHeaderMessage = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', config.getString("messages.disconnect.header")));
        disconnectFooterMessage = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', config.getString("messages.disconnect.footer")));
        ignoredWords = new HashSet<>(config.getStringList("ignored-words"));
    }

    @EventHandler
    public void onPlayerDisconnect(ServerKickEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Server server = player.getServer();
        for (BaseComponent component : event.getKickReasonComponent()) {
            if (ignoredWords.contains(component.toLegacyText())) {
                return;
            }
        }
        if (event.getKickedFrom() != lobbyServer && server != null && !server.getInfo().equals(lobbyServer)) {
            event.setCancelled(true);
            event.setCancelServer(lobbyServer);
            player.sendMessage(disconnectHeaderMessage);
            for (BaseComponent component : event.getKickReasonComponent()) {
                player.sendMessage(new TextComponent(TextComponent.fromLegacyText(
                        ChatColor.translateAlternateColorCodes('&', component.toLegacyText()))));
            }
            player.sendMessage(disconnectFooterMessage);
        }
    }
}
