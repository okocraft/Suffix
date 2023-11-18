package net.okocraft.suffix.core;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import net.okocraft.suffix.core.api.Platform;
import net.okocraft.suffix.core.api.config.SuffixConfig;
import net.okocraft.suffix.core.command.SuffixCommand;

public class SuffixPlugin {

    private final Platform platform;

    private final TranslationManager translationManager;

    private final SuffixConfig config = new SuffixConfig();

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
        try {
            Path filepath = this.platform.getDataFolder().resolve(SuffixConfig.YAML_FILENAME);
            this.platform.saveResource("config.yml", filepath);
            this.platform.loadConfig(this.config, filepath);
        } catch (IOException e) {
            this.platform.getLogger().log(Level.SEVERE, "Could not load config.yml", e);
        }

        suffixCommand = new SuffixCommand(this);
        platform.getServer().registerCommand(suffixCommand);
    }

    public void onDisable() {
        platform.getServer().unregisterCommand(suffixCommand);
        translationManager.unload();
    }

    public Platform platform() {
        return this.platform;
    }

    public SuffixConfig config() {
        return this.config;
    }

}
