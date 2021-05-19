package com.grassminevn.bungeeutils.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyCommand extends Command {
    private final ServerInfo lobbyServer;

    public LobbyCommand() {
        super("lobby", null, "hub");
        lobbyServer = ProxyServer.getInstance().getServerInfo("lobby");
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;
        final ProxiedPlayer player = (ProxiedPlayer) sender;
        player.connect(lobbyServer);
    }
}
