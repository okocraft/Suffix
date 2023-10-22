package net.okocraft.suffix.core.api;

import java.util.logging.Level;

public interface Logger {

    void log(Level level, String msg);
    void log(Level level, String msg, Throwable t);
    void info(String msg);
    void info(String msg, Throwable t);
    void warning(String msg);
    void warning(String msg, Throwable t);
    void severe(String msg);
    void severe(String msg, Throwable t);

}
