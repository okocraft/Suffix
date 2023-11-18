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
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BungeePlatform implements Platform {

    private static final Field CONFIGURATION_MAP_FIELD;

    static {
        try {
            CONFIGURATION_MAP_FIELD = Configuration.class.getDeclaredField("self");
            CONFIGURATION_MAP_FIELD.setAccessible(true);
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

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

    @SuppressWarnings("rawtypes")
    @Override
    public Map<String, String> loadMessages(Path filepath) throws IOException {
        Configuration source = YamlConfiguration.getProvider(YamlConfiguration.class).load(filepath.toFile());

        Map<String, String> result = new HashMap<>();
        Map backingMap;

        try {
            backingMap = (Map) CONFIGURATION_MAP_FIELD.get(source);
        } catch (Throwable e) {
            throw new IOException(e);
        }

        for (String key : source.getKeys()) {
            result.put(key, String.valueOf(backingMap.get(key)));
        }

        return result;
    }
}
