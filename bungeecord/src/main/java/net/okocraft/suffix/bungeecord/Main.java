package net.okocraft.suffix.bungeecord;

import java.io.File;
import net.md_5.bungee.api.plugin.Plugin;
import net.okocraft.suffix.bungeecord.implementation.BungeePlatform;
import net.okocraft.suffix.core.SuffixPlugin;

public class Main extends Plugin {

    private final SuffixPlugin plugin;

    public Main() {
        plugin = new SuffixPlugin(new BungeePlatform(this));
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
