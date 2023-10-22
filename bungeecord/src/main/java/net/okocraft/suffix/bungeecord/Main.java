package net.okocraft.suffix.bungeecord;

import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;
import net.okocraft.suffix.bungeecord.implementation.BungeeLogger;
import net.okocraft.suffix.bungeecord.implementation.BungeePlatform;
import net.okocraft.suffix.core.SuffixPlugin;

public class Main extends Plugin {

    private final SuffixPlugin plugin;

    private final BungeeLogger logger;

    private BungeeAudiences adventure;

    public Main() {
        plugin = new SuffixPlugin(new BungeePlatform(this));
        logger = new BungeeLogger(getLogger());
    }

    @Override
    public void onLoad() {
        plugin.onLoad();
    }

    @Override
    public void onEnable() {
        this.adventure = BungeeAudiences.create(this);
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

    public BungeeAudiences adventure() {
        return this.adventure;
    }

    public BungeeLogger logger() {
        return this.logger;
    }
}
