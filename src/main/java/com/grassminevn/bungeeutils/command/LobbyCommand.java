package com.grassminevn.bungeeutils.command;

import com.grassminevn.bungeeutils.BungeeUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyCommand extends Command {
    private ServerInfo lobbyServer;

    public LobbyCommand(BungeeUtils plugin) {
        super(plugin.getConfigManager().getConfig().getString("lobby-command.command"), null,
                plugin.getConfigManager().getConfig().getStringList("lobby-command.aliases").toArray(new String[0]));
        lobbyServer = ProxyServer.getInstance().getServerInfo("lobby");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer) sender;
        player.connect(lobbyServer);
    }
}
