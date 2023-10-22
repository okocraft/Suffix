package net.okocraft.suffix.bukkit.implementation;

import java.nio.file.Path;
import net.okocraft.suffix.bukkit.Main;
import net.okocraft.suffix.core.api.Logger;
import net.okocraft.suffix.core.api.Platform;
import net.okocraft.suffix.core.api.ServerInterface;

public class BukkitPlatform implements Platform {
    private final Main plugin;

    private final BukkitServerInterface server;

    public BukkitPlatform(Main plugin) {
        this.plugin = plugin;
        this.server = new BukkitServerInterface(plugin);
    }

    @Override
    public Path getDataFolder() {
        return plugin.getDataFolder().toPath();
    }

    @Override
    public String getName() {
        return plugin.getName();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public Logger getLogger() {
        return plugin.logger();
    }

    @Override
    public ServerInterface getServer() {
        return server;
    }

    @Override
    public String getSuffixSetCommand(String playerName, int suffixPriority, String suffix) {
        return "lp user " + playerName + " meta setsuffix " + suffixPriority + " " + suffix;
    }
}
