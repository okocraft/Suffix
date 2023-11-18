package net.okocraft.suffix.bungeecord.implementation;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import net.okocraft.suffix.bungeecord.Main;
import net.okocraft.suffix.core.api.Logger;
import net.okocraft.suffix.core.api.Platform;
import net.okocraft.suffix.core.api.ServerInterface;
import net.okocraft.suffix.core.api.config.SuffixConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class BungeePlatform implements Platform {
    private final Main plugin;

    private final BungeeServerInterface server;

    public BungeePlatform(Main plugin) {
        this.plugin = plugin;
        this.server = new BungeeServerInterface(plugin);
    }

    @Override
    public Path getDataFolder() {
        return plugin.getDataFolder().toPath();
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

    @Override
    public void saveResource(String resourceName, Path filepath) throws IOException {
        if (!Files.isRegularFile(filepath)) {
            try (InputStream input = this.getClass().getClassLoader().getResourceAsStream(resourceName)) {
                Files.copy(Objects.requireNonNull(input), filepath);
            }
        }
    }

    @Override
    public void loadConfig(SuffixConfig config, Path source) throws IOException {
        Configuration bungeeConfig = YamlConfiguration.getProvider(YamlConfiguration.class).load(source.toFile());
        config.suffixMaxLength = bungeeConfig.getInt(SuffixConfig.SUFFIX_MAX_LENGTH_KEY, 0);
        config.suffixPriority = bungeeConfig.getInt(SuffixConfig.SUFFIX_PRIORITY_KEY, 0);
        config.blacklistPatterns = bungeeConfig.getStringList(SuffixConfig.BLACKLIST_PATTERN_KEY);
    }
}
