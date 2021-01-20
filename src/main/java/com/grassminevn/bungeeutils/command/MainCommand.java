package com.grassminevn.bungeeutils.command;

import com.grassminevn.bungeeutils.BungeeUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class MainCommand extends Command {
    private final BungeeUtils plugin;
    private final BaseComponent[] reloadMessage;

    public MainCommand(final BungeeUtils plugin) {
        super("bungeeutils", plugin.getConfigManager().getConfig().getString("permissions.command"), "bu");
        this.plugin = plugin;

        reloadMessage = TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&', plugin.getConfigManager().getConfig().getString("messages.reload")));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) return;
        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadPlugin();
            sender.sendMessage(reloadMessage);
        }
    }
}
