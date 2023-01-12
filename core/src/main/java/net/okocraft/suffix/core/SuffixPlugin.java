package net.okocraft.suffix.core;

import com.github.siroshun09.configapi.api.util.ResourceUtils;
import com.github.siroshun09.configapi.yaml.YamlConfiguration;
import java.io.IOException;
import java.util.logging.Level;
import net.kyori.adventure.platform.AudienceProvider;
import net.okocraft.suffix.core.api.Platform;
import net.okocraft.suffix.core.command.SuffixCommand;

public class SuffixPlugin {

    private final Platform platform;

    private final TranslationManager translationManager;

    private AudienceProvider adventure;

    private YamlConfiguration config;

    private SuffixCommand suffixCommand;

    public SuffixPlugin(Platform platform) {
        this.platform = platform;
        this.translationManager = new TranslationManager(platform);
    }

    public void onLoad() {
        translationManager.load();
    }

    public void onEnable() {
        this.adventure = platform.createAudiences();

        this.config = YamlConfiguration.create(platform.getDataFolder().resolve("config.yml"));
        try {
            ResourceUtils.copyFromJarIfNotExists(platform.getFile().toPath(), "config.yml", config.getPath());
            config.load();
        } catch (IOException e) {
            platform.getLogger().log(Level.SEVERE, "Could not load config.yml", e);
        }

        suffixCommand = new SuffixCommand(this);
        platform.getServer().registerCommand(suffixCommand);
    }

    public void onDisable() {
        platform.getServer().unregisterCommand(suffixCommand);
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }

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

    public AudienceProvider adventure() {
        if (adventure == null) {
            throw new IllegalStateException("Plugin is not enabled yet.");
        }
        return this.adventure;
    }
}
