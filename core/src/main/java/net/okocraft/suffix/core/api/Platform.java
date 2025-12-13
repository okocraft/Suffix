package net.okocraft.suffix.core.api;

import net.okocraft.suffix.core.api.config.SuffixConfig;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public interface Platform {

    Path getDataFolder();

    Logger getLogger();

    ServerInterface getServer();

    void saveResource(String resourceName, Path filepath) throws IOException;

    void loadConfig(SuffixConfig config, Path source) throws IOException;

    Map<String, String> loadMessages(Path filepath) throws IOException;

}
