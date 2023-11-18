package net.okocraft.suffix.core.api;

import net.okocraft.suffix.core.api.config.SuffixConfig;

import java.io.IOException;
import java.nio.file.Path;

public interface Platform {

    Path getDataFolder();

    String getName();

    String getVersion();

    Logger getLogger();

    ServerInterface getServer();

    String getSuffixSetCommand(String playerName, int suffixPriority, String suffix);

    void saveResource(String resourceName, Path filepath) throws IOException;

    void loadConfig(SuffixConfig config, Path source) throws IOException;

}
