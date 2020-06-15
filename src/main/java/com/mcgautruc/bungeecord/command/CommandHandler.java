package com.mcgautruc.bungeecord.command;

import com.mcgautruc.bungeecord.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class CommandHandler extends Command {
    private static final BaseComponent[] UNKNOWN_COMMAND = TextComponent.fromLegacyText("Lệnh không hợp lệ. /help để xem trợ giúp");
    private static final BaseComponent[] USAGE = TextComponent.fromLegacyText("/motd [set|reload]");
    private static final BaseComponent[] MOTD_REMOVED = new ComponentBuilder().color(ChatColor.GREEN).append("Current motd removed.").create();
    private static final BaseComponent[] CONFIG_RELOADED = new ComponentBuilder().color(ChatColor.GREEN).append("Configuration reloaded").create();
    private final BungeeCord plugin;

    public CommandHandler(final BungeeCord plugin) {
        super("motd");
        this.plugin = plugin;
    }

    public void execute(final CommandSender sender, final String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("bungeemotd.set")) {
                    if (args.length == 1) {
                        plugin.getConfig().set("motd", null);
                        sender.sendMessage(MOTD_REMOVED);
                        return;
                    }
                    StringBuilder motd = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        final String arg = args[i] + " ";
                        motd.append(arg);
                    }
                    plugin.getConfig().set("motd", motd.toString());
                    plugin.saveConfig();
                    motd = new StringBuilder(ChatColor.translateAlternateColorCodes('&', motd.toString()));
                    sender.sendMessage(new ComponentBuilder().color(ChatColor.GREEN).append("Current motd set to: ").color(ChatColor.RESET).append(motd.toString()).create());
                    return;
                }
                sender.sendMessage(UNKNOWN_COMMAND);
                return;
            }
            else if ("reload".equals(args[0].toLowerCase())) {
                if (sender.hasPermission("bungeemotd.reload")) {
                    plugin.reloadConfig();
                    sender.sendMessage(CONFIG_RELOADED);
                    return;
                }
                sender.sendMessage(UNKNOWN_COMMAND);
                return;
            }
        }
        sender.sendMessage(USAGE);
    }
}
