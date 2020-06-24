package com.mcgautruc.bungeecord;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventManager implements Listener {
    private static final ServerInfo lobby = ProxyServer.getInstance().getServerInfo("lobby");
    private final BungeeCord plugin;

    EventManager(final BungeeCord plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = 64)
    public void onServerListPing(final ProxyPingEvent event) {
        if (plugin.getConfig().getString("motd") != null) {
            final ServerPing ping = event.getResponse();
            ping.setDescriptionComponent(new TextComponent(TextComponent.fromLegacyText(plugin.getConfig().getString("motd"))));
            if (plugin.getFavicon() != null)
                ping.setFavicon(plugin.getFavicon());
            event.setResponse(ping);
        }
    }

    @EventHandler(priority = 64)
    public void onPlayerDisconnect(final ServerKickEvent event) {
        if (event.getKickedFrom() != lobby) {
            event.setCancelled(true);
            event.setCancelServer(lobby);
            final ProxiedPlayer player = event.getPlayer();
            player.sendMessage(new TextComponent(TextComponent.fromLegacyText(plugin.getConfig().getString("disconnect.header"))));
            player.sendMessage(event.getKickReasonComponent());
            player.sendMessage(new TextComponent(TextComponent.fromLegacyText(plugin.getConfig().getString("disconnect.footer"))));
        }
    }
}
