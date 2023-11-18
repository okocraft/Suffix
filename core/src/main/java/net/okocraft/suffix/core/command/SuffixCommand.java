package net.okocraft.suffix.core.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
import java.util.Locale;
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
        String suffixApplyCommand = plugin.platform().getSuffixSetCommand(
                target.getName(),
                plugin.config().suffixPriority,
                newSuffix
        );

        this.plugin.platform().getServer().dispatchCommand(this.plugin.platform().getServer().getConsole(), suffixApplyCommand); // TODO: LuckPerms API

        sender.sendMessage(messageMap.formatted(
                sender.equals(target) ? Messages.SUCCESS_SELF : Messages.SUCCESS_OTHER,
                Placeholder.component("player", Component.text(target.getName())),
                Placeholder.component("suffix", displaySuffix(newSuffix).hoverEvent(HoverEvent.showText(Component.text(suffix))))
        ));
    }

    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (sender.hasPermission("suffix.other")) {
            if (args.length == 1) {
                return plugin.platform().getServer().getPlayers().stream()
                        .map(CommandSender::getName)
                        .filter((name) -> name.toLowerCase(Locale.ENGLISH).startsWith(args[0].toLowerCase(Locale.ENGLISH)))
                        .collect(Collectors.toList());
            }

            if (args.length == 2) {
                return args[1].isEmpty() ? Collections.singletonList("<suffix>") : Collections.emptyList();
            }
        } else if (args.length == 1) {
            return args[0].isEmpty() ? Collections.singletonList("<suffix>") : Collections.emptyList();
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
