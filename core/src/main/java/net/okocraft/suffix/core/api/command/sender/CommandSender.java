package net.okocraft.suffix.core.api.command.sender;

public abstract class CommandSender {
    public abstract String getName();
    public abstract boolean hasPermission(String permission);

}
