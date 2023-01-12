package net.okocraft.suffix.core.api;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;
import net.kyori.adventure.platform.AudienceProvider;

public interface Platform {

    AudienceProvider createAudiences();

    Path getDataFolder();

    File getFile();

    String getName();

    String getVersion();

    Logger getLogger();

    ServerInterface getServer();

    String getSuffixSetCommand(String playerName, int suffixPriority, String suffix);
}
