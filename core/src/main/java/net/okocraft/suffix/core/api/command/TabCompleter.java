package net.okocraft.suffix.core.api.command;

import java.util.List;
import net.okocraft.suffix.core.api.command.sender.CommandSender;

public interface TabCompleter {

    List<String> onTabComplete(CommandSender sender, String[] args);
}
