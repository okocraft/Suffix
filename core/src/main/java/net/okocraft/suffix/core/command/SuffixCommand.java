package net.okocraft.suffix.core.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.SuffixNode;
import net.okocraft.suffix.core.SuffixPlugin;
import net.okocraft.suffix.core.api.command.Command;
import net.okocraft.suffix.core.api.command.TabCompleter;
import net.okocraft.suffix.core.api.command.sender.CommandSender;
import net.okocraft.suffix.core.api.command.sender.ConsoleCommandSender;
import net.okocraft.suffix.core.api.command.sender.Player;
import net.okocraft.suffix.core.message.Messages;
import net.okocraft.suffix.core.message.MessageMap;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SuffixCommand extends Command implements TabCompleter {

    private final SuffixPlugin plugin;

    public SuffixCommand(SuffixPlugin plugin) {
        super("suffix");
        this.plugin = plugin;
    }

    // /suffix <suffix> [player]
    public void execute(CommandSender sender, String[] args) {
        MessageMap messageMap = this.plugin.getLocalizedMessageMap(sender.getLocale());
        if (!sender.hasPermission("suffix.use")) {
            sender.sendMessage(messageMap.formatted(Messages.NO_PERMISSION));
            return;
        }

        String suffix;
        Player target;

        if (sender instanceof ConsoleCommandSender) {
            if (2 <= args.length) {
                suffix = args[0];
                target = this.plugin.platform().getServer().getPlayer(args[1]);
            } else {
                sender.sendMessage(messageMap.formatted(Messages.COMMAND_HELP_CONSOLE));
                return;
            }
        } else if (2 <= args.length) {
            if (sender.hasPermission("suffix.other")) {
                suffix = args[0];
                target = this.plugin.platform().getServer().getPlayer(args[1]);
            } else {
                sender.sendMessage(messageMap.formatted(Messages.NO_PERMISSION));
                return;
            }
        } else if (args.length == 1) {
            suffix = args[0];
            target = (Player) sender;
        } else {
            String messageKey = sender.hasPermission("suffix.other") ? Messages.COMMAND_HELP_WITH_OTHER_ARGUMENT : Messages.COMMAND_HELP_DEFAULT;
            sender.sendMessage(messageMap.formatted(messageKey));
            return;
        }

        if (!sender.hasPermission("suffix.unlimited.length")) {
            if (this.plugin.config().suffixMaxLength < calculateLength(suffix)) {
                sender.sendMessage(messageMap.formatted(
                        Messages.TOO_LONG_SUFFIX,
                        Placeholder.component("arg", Component.text(suffix)),
                        Placeholder.component("length", Component.text(this.plugin.config().suffixMaxLength))
                ));
                return;
            }
        }

        if (!sender.hasPermission("suffix.unlimited.pattern")) {
            for (String blacklistPattern : plugin.config().blacklistPatterns) {
                if (suffix.contains(blacklistPattern)) {
                    sender.sendMessage(messageMap.formatted(Messages.BLACKLIST_PATTERN, Placeholder.component("pattern", Component.text(blacklistPattern))));
                    return;
                }
            }
        }

        if (target == null) {
            sender.sendMessage(messageMap.formatted(Messages.PLAYER_NOT_FOUND, Placeholder.component("arg", Component.text(args[1]))));
            return;
        }

        String newSuffix = suffix.replace("&k", "");
        int priority = this.plugin.config().suffixPriority;

        LuckPermsProvider.get().getUserManager().modifyUser(
                target.getUniqueId(),
                user -> {
                    user.getNodes(NodeType.SUFFIX).stream()
                            .filter(node -> node.getPriority() == priority)
                            .forEach(user.data()::remove);
                    user.data().add(SuffixNode.builder(newSuffix, priority).build());
                }
        );

        sender.sendMessage(messageMap.formatted(
                sender.equals(target) ? Messages.SUCCESS_SELF : Messages.SUCCESS_OTHER,
                Placeholder.component("player", Component.text(target.getName())),
                Placeholder.component("suffix", displaySuffix(newSuffix).hoverEvent(HoverEvent.showText(Component.text(suffix))))
        ));
    }

    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (!sender.hasPermission("suffix.use")) {
            return Collections.emptyList();
        }

        if (args.length <= 1) {
            return args.length == 0 || args[0].isEmpty() ? Collections.singletonList("<suffix>") : Collections.emptyList();
        }

        if (args.length == 2 && sender.hasPermission("suffix.other")) {
            int argLength = args[1].length();
            return this.plugin.platform().getServer().getPlayers().stream()
                    .map(CommandSender::getName)
                    .filter(name -> name.regionMatches(true, 0, args[1], 0, argLength))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private static int calculateLength(String suffix) {
        return PlainTextComponentSerializer.plainText().serialize(displaySuffix(suffix)).length();
    }

    private static Component displaySuffix(String suffix) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(suffix);
    }
}
