package net.okocraft.suffix.bukkit;

import java.io.File;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.okocraft.suffix.bukkit.implementation.BukkitPlatform;
import net.okocraft.suffix.core.SuffixPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private final SuffixPlugin plugin;

    private BukkitAudiences adventure;

    public Main() {
        plugin = new SuffixPlugin(new BukkitPlatform(this));
    }

    @Override
    public void onLoad() {
        plugin.onLoad();
    }

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        plugin.onEnable();
    }

    @Override
    public void onDisable() {
        plugin.onDisable();
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public BukkitAudiences adventure() {
        return this.adventure;
    }

    // Fu
    public File getJarFile() {
        return getFile();
    }
}
