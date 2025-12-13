package net.okocraft.suffix.core.api.command;

import java.util.Objects;
import net.okocraft.suffix.core.api.command.sender.CommandSender;

public abstract class Command {
    private final String name;

    public Command(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    public abstract void execute(CommandSender sender, String[] args);

    public String getName() {
        return this.name;
    }

}
