package net.okocraft.suffix.core;

import com.github.siroshun09.configapi.api.Configuration;
import com.github.siroshun09.configapi.api.util.ResourceUtils;
import com.github.siroshun09.configapi.yaml.YamlConfiguration;
import com.github.siroshun09.translationloader.ConfigurationLoader;
import com.github.siroshun09.translationloader.TranslationLoader;
import com.github.siroshun09.translationloader.directory.TranslationDirectory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import net.kyori.adventure.key.Key;
import net.okocraft.suffix.core.api.Platform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TranslationManager {

    private final Platform plugin;
    private final Path jarFile;
    private final TranslationDirectory translationDirectory;

    TranslationManager(Platform plugin) {
        this.plugin = plugin;
        String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
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
        this.jarFile = jarFilePath;

        Path pluginDirectory = plugin.getDataFolder();

        this.translationDirectory = TranslationDirectory.newBuilder()
                .setDirectory(pluginDirectory.resolve("languages"))
                .setKey(Key.key(plugin.getName().toLowerCase(Locale.ROOT), "languages"))
                .setDefaultLocale(Locale.ENGLISH)
                .onDirectoryCreated(this::saveDefaultLanguages)
                .setVersion(plugin.getVersion())
                .setTranslationLoaderCreator(this::getBundledTranslation)
                .build();
    }

    void load() {
        try {
            translationDirectory.load();
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not load languages", e);
        }
    }

    void unload() {
        translationDirectory.unload();
    }

    private void saveDefaultLanguages(@NotNull Path directory) throws IOException {
        List<String> files = Arrays.asList(
                "en.yml",
                "ja_JP.yml"
        );
        for (String file : files) {
            ResourceUtils.copyFromJarIfNotExists(jarFile, ("languages/" + file), directory.resolve(file));
        }
    }

    private @Nullable TranslationLoader getBundledTranslation(@NotNull Locale locale) throws IOException {
        String strLocale = locale.toString();

        if (!(strLocale.equals("en") || strLocale.equals("ja_JP"))) {
            return null;
        }

        Configuration source;

        try (InputStream input = ResourceUtils.getInputStreamFromJar(jarFile, "languages/" + strLocale + ".yml")) {
            source = YamlConfiguration.loadFromInputStream(input);
        }

        TranslationLoader loader = ConfigurationLoader.create(locale, source);
        loader.load();

        return loader;
    }
}
