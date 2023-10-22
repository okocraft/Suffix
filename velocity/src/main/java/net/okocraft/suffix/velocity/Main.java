package net.okocraft.suffix.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import java.nio.file.Path;
import javax.inject.Named;
import net.okocraft.suffix.core.api.Logger;
import net.okocraft.suffix.velocity.implementation.VelocityLogger;
import net.okocraft.suffix.velocity.implementation.VelocityPlatform;
import net.okocraft.suffix.core.SuffixPlugin;

public class Main {

    private final SuffixPlugin plugin;
    private final ProxyServer proxy;
    private final PluginContainer container;
    private final VelocityLogger logger;
    private final Path dataDirectory;

    @Inject
    public Main(ProxyServer proxy, @Named("suffix") PluginContainer container, org.slf4j.Logger logger, @DataDirectory Path dataDirectory) {
        plugin = new SuffixPlugin(new VelocityPlatform(this));
        this.proxy = proxy;
        this.container = container;
        this.logger = new VelocityLogger(logger);
        this.dataDirectory = dataDirectory;
        plugin.onLoad();
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onEnable(ProxyInitializeEvent event) {
        plugin.onEnable();
    }

    @Subscribe(order = PostOrder.LAST)
    public void onDisable(ProxyShutdownEvent event) {
        plugin.onDisable();
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
