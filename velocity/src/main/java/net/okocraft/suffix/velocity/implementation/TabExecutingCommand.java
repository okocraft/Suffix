package net.okocraft.suffix.velocity.implementation;

import com.velocitypowered.api.command.SimpleCommand;

public abstract class TabExecutingCommand implements SimpleCommand {

    private final String name;

    public TabExecutingCommand(String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }
}
