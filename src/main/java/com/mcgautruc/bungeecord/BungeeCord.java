package com.mcgautruc.bungeecord;

import com.mcgautruc.bungeecord.command.CommandHandler;
import com.mcgautruc.bungeecord.command.LobbyCommand;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BungeeCord extends Plugin implements Listener {
    private Configuration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        getProxy().getPluginManager().registerListener(this, new EventManager(this));
        getProxy().getPluginManager().registerCommand(this, new CommandHandler(this));
        getProxy().getPluginManager().registerCommand(this, new LobbyCommand());
    }

    public Configuration getConfig() {
        return config;
    }

    public void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (final IOException e) {
            throw new RuntimeException("Unable to load configuration", e);
        }
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "config.yml"));
        }
        catch (final IOException e) {
            throw new RuntimeException("Unable to save configuration", e);
        }
    }

    private void saveDefaultConfig() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        final File file = new File(getDataFolder(), "config.yml");
        if (file.exists()) return;
        try (final BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
            stream.write(readAllBytes(getResourceAsStream("config.yml")));
            stream.flush();
        } catch (final IOException e) {
            throw new RuntimeException("Unable to create configuration file", e);
        }
    }

    private byte[] readAllBytes(final InputStream stream) throws IOException {
        List<byte[]> bufs = null;
        byte[] result = null;
        int total = 0;
        int remaining = Integer.MAX_VALUE;
        int n;
        do {
            final byte[] buf = new byte[Math.min(remaining, 8192)];
            int nread = 0;

            // read to EOF which may read more or less than buffer size
            while ((n = stream.read(buf, nread,
                             Math.min(buf.length - nread, remaining))) > 0) {
                nread += n;
                remaining -= n;
            }

            if (nread > 0) {
                if (Integer.MAX_VALUE - 8 - total < nread) {
                    throw new OutOfMemoryError("Required array size too large");
                }
                total += nread;
                if (result == null) {
                    result = buf;
                } else {
                    if (bufs == null) {
                        bufs = new ArrayList<>();
                        bufs.add(result);
                    }
                    bufs.add(buf);
                }
            }
            // if the last call to read returned -1 or the number of bytes
            // requested have been read then break
        } while (n >= 0 && remaining > 0);

        if (bufs == null) {
            if (result == null) {
                return new byte[0];
            }
            return result.length == total ?
                    result : Arrays.copyOf(result, total);
        }

        result = new byte[total];
        int offset = 0;
        remaining = total;
        for (final byte[] b : bufs) {
            final int count = Math.min(b.length, remaining);
            System.arraycopy(b, 0, result, offset, count);
            offset += count;
            remaining -= count;
        }

        return result;
    }
}
