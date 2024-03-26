package net.okocraft.suffix.core.api.command.sender;

import net.kyori.adventure.text.Component;

import java.util.Locale;

public abstract class CommandSender {
    public abstract String getName();
    public abstract boolean hasPermission(String permission);
    public Locale getLocale() {
        return Locale.getDefault();
    }
    public abstract void sendMessage(Component message);

}
