package net.okocraft.suffix.core;

import com.github.siroshun09.translationloader.ConfigurationLoader;
import com.github.siroshun09.translationloader.TranslationLoader;
import com.github.siroshun09.translationloader.directory.TranslationDirectory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarFile;

import net.kyori.adventure.key.Key;

public class TranslationManager {

    private final Path jarFilePath;
    private final TranslationDirectory translationDirectory;

    public TranslationManager(String pluginName, String pluginVersion, Path jarFilePath, Path pluginDirectory) {
        this.jarFilePath = jarFilePath;
        this.translationDirectory = TranslationDirectory.newBuilder()
                .setDirectory(pluginDirectory.resolve("languages"))
                .setKey(Key.key(pluginName.toLowerCase(Locale.ROOT), "languages"))
                .setDefaultLocale(Locale.ENGLISH)
                .onDirectoryCreated(this::saveDefaultLanguages)
                .setVersion(pluginVersion)
                .setTranslationLoaderCreator(this::getBundledTranslation)
                .build();
    }

    public void load() {
        try {
            translationDirectory.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unload() {
        translationDirectory.unload();
    }

    private void saveDefaultLanguages(Path directory) throws IOException {
        List<String> files = Arrays.asList(
                "en.yml",
                "ja_JP.yml"
        );
        for (String file : files) {
            // ResourceUtils.copyFromJarIfNotExists(jarFilePath, ("languages/" + file), directory.resolve(file));
        }
    }

    private TranslationLoader getBundledTranslation(Locale locale) throws IOException {
        String strLocale = locale.toString();

        if (!(strLocale.equals("en") || strLocale.equals("ja_JP"))) {
            return null;
        }

        /*
        Configuration source;

        try (JarFile jar = new JarFile(jarFilePath.toFile());
             InputStream input = ResourceUtils.getInputStreamFromJar(jar, "languages/" + strLocale + ".yml")) {
            source = YamlConfiguration.loadFromInputStream(input);
        }

        TranslationLoader loader = ConfigurationLoader.create(locale, source);
        loader.load();
         */
        return null;
    }
}
