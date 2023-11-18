package net.okocraft.suffix.bukkit.implementation;

import java.nio.file.Path;

import net.okocraft.suffix.bukkit.Main;
import net.okocraft.suffix.core.api.Logger;
import net.okocraft.suffix.core.api.Platform;
import net.okocraft.suffix.core.api.ServerInterface;
import net.okocraft.suffix.core.api.config.SuffixConfig;
import org.bukkit.configuration.file.FileConfiguration;

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

    @Override
    public void saveResource(String resourceName, Path filepath) throws IOException {
        if (!Files.isRegularFile(filepath)) {
            Files.copy(this.plugin.getResource(resourceName), filepath);
        }
    }

    @Override
    public void loadConfig(SuffixConfig config, Path source) {
        this.plugin.reloadConfig();

        FileConfiguration bukkitConfig = this.plugin.getConfig();
        config.suffixMaxLength = bukkitConfig.getInt(SuffixConfig.SUFFIX_MAX_LENGTH_KEY, 0);
        config.suffixPriority = bukkitConfig.getInt(SuffixConfig.SUFFIX_PRIORITY_KEY, 0);
        config.blacklistPatterns = bukkitConfig.getStringList(SuffixConfig.BLACKLIST_PATTERN_KEY);
    }
}
