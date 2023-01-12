package net.okocraft.suffix.core.api.command;

import java.util.Objects;
import net.okocraft.suffix.core.api.command.sender.CommandSender;

public abstract class Command {
    private final String name;
    private final String permission;
    private final String[] aliases;
    private String permissionMessage;

    public Command(String name) {
        this(name, null);
    }

    public Command(String name, String permission, String... aliases) {
        this.name = Objects.requireNonNull(name, "name");
        this.permission = permission;
        this.aliases = aliases;
        this.permissionMessage = null;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public boolean hasPermission(CommandSender sender) {
        return permission == null || permission.isEmpty() || sender.hasPermission(permission);
    }

    public String getName() {
        return this.name;
    }

    public String getPermission() {
        return this.permission;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public String getPermissionMessage() {
        return this.permissionMessage;
    }
}
