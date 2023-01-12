package net.okocraft.suffix.core.api;

import java.util.Collection;
import java.util.UUID;
import net.okocraft.suffix.core.api.command.Command;
import net.okocraft.suffix.core.api.command.sender.CommandSender;
import net.okocraft.suffix.core.api.command.sender.Player;

public interface ServerInterface {

    void registerCommand(Command command);

    void unregisterCommand(Command command);

    void dispatchCommand(CommandSender sender, String command);

    Player getPlayer(String name);
    Player getPlayer(UUID uniqueId);

    CommandSender getConsole();

    Collection<Player> getPlayers();
}
