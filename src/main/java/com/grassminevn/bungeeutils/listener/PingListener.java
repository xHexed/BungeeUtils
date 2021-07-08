package com.grassminevn.bungeeutils.listener;

import com.grassminevn.bungeeutils.BungeeUtils;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PingListener implements Listener {
    private String serverType;

    public PingListener(BungeeUtils plugin) {
        serverType = plugin.getConfigManager().getConfig().getString("modify-server-info.server-type");
    }

    @EventHandler(priority = 64)
    public void onProxyPing(ProxyPingEvent e) {
        if (e.getResponse() == null)
            return;
        ServerPing ping = e.getResponse();
        ping.getModinfo().setType(serverType);
        e.setResponse(ping);
    }
}
