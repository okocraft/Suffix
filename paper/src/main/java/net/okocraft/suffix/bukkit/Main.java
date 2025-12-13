package net.okocraft.suffix.bukkit;

import net.okocraft.suffix.bukkit.implementation.BukkitLogger;
import net.okocraft.suffix.bukkit.implementation.BukkitPlatform;
import net.okocraft.suffix.core.SuffixPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private final SuffixPlugin plugin;
    private final BukkitLogger logger;

    public Main() {
        plugin = new SuffixPlugin(new BukkitPlatform(this));
        logger = new BukkitLogger(getLogger());
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

    public BukkitLogger logger() {
        return this.logger;
    }
}
