package com.mcgautruc.bungeecord;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventManager implements Listener {
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
}
