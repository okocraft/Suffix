package net.okocraft.suffix.bukkit;

import java.io.File;
import net.okocraft.suffix.bukkit.implementation.BukkitPlatform;
import net.okocraft.suffix.core.SuffixPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private final SuffixPlugin plugin;

    public Main() {
        plugin = new SuffixPlugin(new BukkitPlatform(this));
    }

    @Override
    public void onLoad() {
        plugin.onLoad();
    }

    @Override
    public void onEnable() {
        plugin.onEnable();
    }

    @Override
    public void onDisable() {
        plugin.onDisable();
    }

    // Fu
    public File getJarFile() {
        return getFile();
    }
}
