package net.okocraft.suffix.core;

import net.kyori.adventure.translation.Translator;
import net.okocraft.suffix.core.api.Platform;
import net.okocraft.suffix.core.api.config.SuffixConfig;
import net.okocraft.suffix.core.command.SuffixCommand;
import net.okocraft.suffix.core.message.MessageMap;
import net.okocraft.suffix.core.message.Messages;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class SuffixPlugin {

    private final Platform platform;

    private final Map<Locale, MessageMap> localizationMap = new HashMap<>();
    private final MessageMap defaultMessageMap = new MessageMap(Messages.getDefaultMessages());

    private final SuffixConfig config = new SuffixConfig();

    private SuffixCommand suffixCommand;

    public SuffixPlugin(Platform platform) {
        this.platform = platform;
    }

    public void onLoad() {
        try {
            Path directory = this.platform.getDataFolder().resolve("languages");
            Files.createDirectories(directory);
            this.platform.saveResource("languages/en.yml", directory.resolve("en.yml"));
            this.platform.saveResource("languages/ja_JP.yml", directory.resolve("ja_JP.yml"));
            try (Stream<Path> list = Files.list(directory)) {
                list.forEach(this::loadMessage);
            }
        } catch (IOException | UncheckedIOException e) {
            this.platform.getLogger().error("Could not load messages", e);
        }
    }

    public void onEnable() {
        try {
            Path filepath = this.platform.getDataFolder().resolve(SuffixConfig.YAML_FILENAME);
            this.platform.saveResource("config.yml", filepath);
            this.platform.loadConfig(this.config, filepath);
        } catch (IOException e) {
            this.platform.getLogger().error("Could not load config.yml", e);
        }

        suffixCommand = new SuffixCommand(this);
        platform.getServer().registerCommand(suffixCommand);
    }

    public void onDisable() {
        platform.getServer().unregisterCommand(suffixCommand);
    }

    public Platform platform() {
        return this.platform;
    }

    public SuffixConfig config() {
        return this.config;
    }

    public MessageMap getLocalizedMessageMap(Locale locale) {
        if (locale == null) {
            return this.defaultMessageMap;
        }

        MessageMap messageMap = this.localizationMap.get(locale);

        if (messageMap == null) {
            messageMap = this.localizationMap.get(Locale.of(locale.getLanguage()));
        }

        return messageMap != null ? messageMap : this.defaultMessageMap;
    }

    private void loadMessage(Path filepath) {
        String filename = filepath.getFileName().toString();
        if (!filename.endsWith(".yml")) {
            return;
        }

        Locale locale = Translator.parseLocale(filename.substring(0, filename.length() - 4));

        if (locale == null) {
            return;
        }

        Map<String, String> loadedMessages;

        try {
            loadedMessages = this.platform.loadMessages(filepath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        for (Map.Entry<String, String> defaultMessage : Messages.getDefaultMessages().entrySet()) {
            loadedMessages.putIfAbsent(defaultMessage.getKey(), defaultMessage.getValue());
        }

        var messageMap = new MessageMap(loadedMessages);
        this.localizationMap.put(locale, messageMap);
        this.localizationMap.put(Locale.of(locale.getLanguage()), messageMap);
    }
}
