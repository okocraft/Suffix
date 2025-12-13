package net.okocraft.suffix.velocity.implementation;

import net.okocraft.suffix.core.api.Platform;
import net.okocraft.suffix.core.api.ServerInterface;
import net.okocraft.suffix.core.api.config.SuffixConfig;
import net.okocraft.suffix.velocity.Main;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
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
    public Logger getLogger() {
        return plugin.logger();
    }

    @Override
    public ServerInterface getServer() {
        return server;
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
        ConfigurationNode node = YamlConfigurationLoader.builder().path(source).build().load();
        config.suffixMaxLength = node.node(SuffixConfig.SUFFIX_MAX_LENGTH_KEY).getInt();
        config.suffixPriority = node.node(SuffixConfig.SUFFIX_PRIORITY_KEY).getInt();
        config.blacklistPatterns = node.node(SuffixConfig.BLACKLIST_PATTERN_KEY).getList(String.class);
    }

    @Override
    public Map<String, String> loadMessages(Path filepath) throws IOException {
        ConfigurationNode source = YamlConfigurationLoader.builder().path(filepath).build().load();

        Map<String, String> result = new HashMap<>();

        for (Map.Entry<Object, ? extends ConfigurationNode> entry : source.childrenMap().entrySet()) {
            result.put(String.valueOf(entry.getKey()), entry.getValue().getString());
        }

        return result;
    }
}
