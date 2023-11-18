package net.okocraft.suffix.velocity.implementation;

import net.okocraft.suffix.core.api.Logger;
import net.okocraft.suffix.core.api.Platform;
import net.okocraft.suffix.core.api.ServerInterface;
import net.okocraft.suffix.core.api.config.SuffixConfig;
import net.okocraft.suffix.velocity.Main;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

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
        return "lpv user " + playerName + " meta setsuffix " + suffixPriority + " " + suffix;
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
        ConfigurationNode node = YAMLConfigurationLoader.builder().setPath(source).build().load();
        config.suffixMaxLength = node.getNode(SuffixConfig.SUFFIX_MAX_LENGTH_KEY).getInt();
        config.suffixPriority = node.getNode(SuffixConfig.SUFFIX_PRIORITY_KEY).getInt();
        config.blacklistPatterns = node.getNode(SuffixConfig.BLACKLIST_PATTERN_KEY).getList(String::valueOf);
    }
}
