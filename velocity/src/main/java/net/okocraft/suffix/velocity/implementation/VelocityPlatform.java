package net.okocraft.suffix.velocity.implementation;

import java.nio.file.Path;
import net.okocraft.suffix.core.api.Logger;
import net.okocraft.suffix.velocity.Main;
import net.okocraft.suffix.core.api.Platform;
import net.okocraft.suffix.core.api.ServerInterface;

public class VelocityPlatform implements Platform {
    private final Main plugin;

    private final VelocityServerInterface server;

    public VelocityPlatform(Main plugin) {
        this.plugin = plugin;
        this.server = new VelocityServerInterface(plugin);
    }

    @Override
    public Path getDataFolder() {
        return plugin.dataDirectory();
    }

    @Override
    public String getName() {
        return plugin.container().getDescription().getName().orElse("");
    }

    @Override
    public String getVersion() {
        return plugin.container().getDescription().getVersion().orElse("");
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
        return "lpb user " + playerName + " meta setsuffix " + suffixPriority + " " + suffix;
    }
}
