package net.okocraft.suffix.core.api;

import java.util.Collection;
import net.okocraft.suffix.core.api.command.Command;
import net.okocraft.suffix.core.api.command.sender.Player;

public interface ServerInterface {

    void registerCommand(Command command);

    void unregisterCommand(Command command);

    Player getPlayer(String name);

    Collection<Player> getPlayers();
}
