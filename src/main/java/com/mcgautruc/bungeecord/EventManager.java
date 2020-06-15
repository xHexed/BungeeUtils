package com.mcgautruc.bungeecord;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
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

    @EventHandler(priority = 127)
    public void onPlayerTab(final TabCompleteEvent event) {
        if (event.isCancelled() || !(event.getSender() instanceof ProxiedPlayer))
            return;
        final CommandSender player = (CommandSender) event.getSender();
        if (player.hasPermission("admin")) return;
        String command = event.getCursor().split(" ")[0].toLowerCase();
        if (command.length() < 1)
            return;
        command = command.substring(1);
        if (player.hasPermission("command." + command))
            return;
        if (plugin.getCommands().contains(command.toLowerCase()))
            event.setCancelled(true);
    }


    @EventHandler(priority = 127)
    public void onPlayerChat(final ChatEvent event) {
        if (event.isCancelled() || !(event.getSender() instanceof ProxiedPlayer) || !event.isCommand())
            return;
        final CommandSender player = (CommandSender) event.getSender();
        if (player.hasPermission("admin"))
            return;
        String command = event.getMessage().split(" ")[0].toLowerCase();
        if (command.length() < 1)
            return;
        command = command.substring(1);
        if (player.hasPermission("command." + command))
            return;
        if (plugin.getCommands().contains(command.toLowerCase())) {
            event.setCancelled(true);
            player.sendMessage(new TextComponent(TextComponent.fromLegacyText("Lệnh không hợp lệ. /help để xem trợ giúp")));
        }
    }
}
