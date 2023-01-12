package net.okocraft.suffix.bungeecord.implementation;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.okocraft.suffix.bungeecord.Main;
import net.okocraft.suffix.core.api.Platform;
import net.okocraft.suffix.core.api.ServerInterface;

public class BungeePlatform implements Platform {
    private final Main plugin;

    private final BungeeServerInterface server;

    public BungeePlatform(Main plugin) {
        this.plugin = plugin;
        this.server = new BungeeServerInterface(plugin);
    }

    @Override
    public AudienceProvider createAudiences() {
        return BungeeAudiences.create(plugin);
    }

    @Override
    public Path getDataFolder() {
        return plugin.getDataFolder().toPath();
    }

    @Override
    public File getFile() {
        return plugin.getJarFile();
    }

    @Override
    public String getName() {
        return plugin.getDescription().getName();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public Logger getLogger() {
        return plugin.getLogger();
    }

    @Override
    public ServerInterface getServer() {
        return server;
    }

    @Override
    public String getSuffixSetCommand(String playerName, int suffixPriority, String suffix) {
        return "lpb user " + playerName + " meta setsuffix " + suffixPriority + " " + suffix;
    }
}
