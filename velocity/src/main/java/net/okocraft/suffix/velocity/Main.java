package net.okocraft.suffix.velocity;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.okocraft.suffix.core.SuffixPlugin;
import net.okocraft.suffix.velocity.implementation.VelocityPlatform;
import org.slf4j.Logger;

import java.nio.file.Path;

public class Main {


    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;
    private final PluginContainer container;

    private SuffixPlugin plugin;

    @Inject
    public Main(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory, @Named("suffix") PluginContainer container) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.container = container;
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onEnable(ProxyInitializeEvent event) {
        this.plugin = new SuffixPlugin(new VelocityPlatform(this));
        this.plugin.onLoad();
        this.plugin.onEnable();
    }

    @Subscribe(order = PostOrder.LAST)
    public void onDisable(ProxyShutdownEvent event) {
        this.plugin.onDisable();
    }

    public ProxyServer proxy() {
        return this.proxy;
    }

    public PluginContainer container() {
        return this.container;
    }

    public Logger logger() {
        return this.logger;
    }

    public Path dataDirectory() {
        return this.dataDirectory;
    }
}
