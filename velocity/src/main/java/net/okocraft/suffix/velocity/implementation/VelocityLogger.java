package net.okocraft.suffix.velocity.implementation;

import java.util.logging.Level;
import net.okocraft.suffix.core.api.Logger;

public class VelocityLogger implements Logger {

    private final org.slf4j.Logger log;

    public VelocityLogger(org.slf4j.Logger log) {
        this.log = log;
    }

    @Override
    public void log(Level level, String msg) {
        if (level == Level.INFO || level == Level.FINE || level == Level.FINER || level == Level.FINEST || level == Level.ALL) {
            log.info(msg);
        } else if (level == Level.WARNING) {
            log.warn(msg);
        } else if (level == Level.SEVERE) {
            log.error(msg);
        }
    }

    @Override
    public void log(Level level, String msg, Throwable t) {
        if (level == Level.INFO || level == Level.FINE || level == Level.FINER || level == Level.FINEST || level == Level.ALL) {
            log.info(msg, t);
        } else if (level == Level.WARNING) {
            log.warn(msg, t);
        } else if (level == Level.SEVERE) {
            log.error(msg, t);
        }
    }

    @Override
    public void info(String msg) {
        log.info(msg);
    }

    @Override
    public void info(String msg, Throwable t) {
        log.info(msg, t);
    }

    @Override
    public void warning(String msg) {
        log.warn(msg);
    }

    @Override
    public void warning(String msg, Throwable t) {
        log.warn(msg, t);
    }

    @Override
    public void severe(String msg) {
        log.error(msg);
    }

    @Override
    public void severe(String msg, Throwable t) {
        log.error(msg, t);
    }
}
