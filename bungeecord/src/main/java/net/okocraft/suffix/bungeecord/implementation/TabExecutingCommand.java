package net.okocraft.suffix.bungeecord.implementation;

import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public abstract class TabExecutingCommand extends Command implements TabExecutor {
    public TabExecutingCommand(String name) {
        super(name);
    }
}
