package net.okocraft.suffix.core;

import com.github.siroshun09.configapi.api.util.ResourceUtils;
import com.github.siroshun09.configapi.yaml.YamlConfiguration;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import net.okocraft.suffix.core.api.Platform;
import net.okocraft.suffix.core.command.SuffixCommand;

public class SuffixPlugin {

    private final Platform platform;

    private final TranslationManager translationManager;

    private YamlConfiguration config;

    private SuffixCommand suffixCommand;

    public SuffixPlugin(Platform platform) {
        this.platform = platform;
        this.translationManager = new TranslationManager(
                platform.getName(),
                platform.getVersion(),
                getJarPath(),
                platform.getDataFolder()
        );
    }

    public void onLoad() {
        translationManager.load();
    }

    public void onEnable() {
        this.config = YamlConfiguration.create(platform.getDataFolder().resolve("config.yml"));
        try {
            ResourceUtils.copyFromJarIfNotExists(getJarPath(), "config.yml", config.getPath());
            config.load();
        } catch (IOException e) {
            platform.getLogger().log(Level.SEVERE, "Could not load config.yml", e);
        }

        suffixCommand = new SuffixCommand(this);
        platform.getServer().registerCommand(suffixCommand);
    }

    public void onDisable() {
        platform.getServer().unregisterCommand(suffixCommand);
        translationManager.unload();

        if (this.config != null) {
            this.config.close();
            this.config = null;
        }
    }

    public Platform platform() {
        return this.platform;
    }

    public YamlConfiguration config() {
        if (this.config == null) {
            throw new IllegalStateException("Plugin is not enabled yet.");
        }
        return this.config;
    }

    private static Path getJarPath() {
        String path = SuffixPlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        Path jarFilePath;
        try {
            // for linux.
            jarFilePath = Paths.get(path);
        } catch (InvalidPathException e) {
            // for windows.
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            jarFilePath = Paths.get(path);
        }
        return jarFilePath;
    }
}
