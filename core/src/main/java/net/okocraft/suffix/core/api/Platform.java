package net.okocraft.suffix.core.api;

import java.nio.file.Path;

public interface Platform {

    Path getDataFolder();

    String getName();

    String getVersion();

    Logger getLogger();

    ServerInterface getServer();

    String getSuffixSetCommand(String playerName, int suffixPriority, String suffix);
}
