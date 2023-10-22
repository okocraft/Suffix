package net.okocraft.suffix.bungeecord.implementation;

import java.util.logging.Level;
import net.okocraft.suffix.core.api.Logger;

public class BungeeLogger implements Logger {

    private final java.util.logging.Logger log;

    public BungeeLogger(java.util.logging.Logger log) {
        this.log = log;
    }

    @Override
    public void log(Level level, String msg) {
        log.log(level, msg);
    }

    @Override
    public void log(Level level, String msg, Throwable t) {
        log.log(level, msg, t);
    }

    @Override
    public void info(String msg) {
        log.log(Level.INFO, msg);
    }

    @Override
    public void info(String msg, Throwable t) {
        log.log(Level.INFO, msg, t);
    }

    @Override
    public void warning(String msg) {
        log.log(Level.WARNING, msg);
    }

    @Override
    public void warning(String msg, Throwable t) {
        log.log(Level.WARNING, msg, t);
    }

    @Override
    public void severe(String msg) {
    }

    @Override
    public void severe(String msg, Throwable t) {
        log.log(Level.SEVERE, msg, t);
    }
}
