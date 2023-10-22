package net.okocraft.suffix.core.api.command.sender;

import net.kyori.adventure.text.Component;

public abstract class CommandSender {
    public abstract String getName();
    public abstract boolean hasPermission(String permission);

    public abstract void sendMessage(Component message);

}
