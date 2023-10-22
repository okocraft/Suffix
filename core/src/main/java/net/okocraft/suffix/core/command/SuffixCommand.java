package net.okocraft.suffix.core.command;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.okocraft.suffix.core.SuffixPlugin;
import net.okocraft.suffix.core.api.command.sender.CommandSender;
import net.okocraft.suffix.core.api.command.sender.Player;
import net.okocraft.suffix.core.api.ServerInterface;
import net.okocraft.suffix.core.api.command.Command;
import net.okocraft.suffix.core.api.command.TabCompleter;

public class SuffixCommand extends Command implements TabCompleter {

    private final SuffixPlugin plugin;

    public SuffixCommand(SuffixPlugin plugin) {
        super("suffix");
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("suffix.use")) {
            sender.sendMessage(Component.translatable("no-permission"));
            return;
        }

        ServerInterface server = plugin.platform().getServer();
        String suffix;
        Player target;
        if (sender.hasPermission("suffix.other")) {
            if (args.length == 0) {
                sender.sendMessage(Component.translatable("specify-player"));
                return;
            }

            if (args.length == 1) {
                sender.sendMessage(Component.translatable("specify-suffix"));
                return;
            }

            target = server.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Component.translatable("player-not-found"));
                return;
            }

            suffix = args[1];
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Component.translatable("player-only"));
                return;
            }
            Player player = ((Player) sender);

            if (args.length == 0) {
                sender.sendMessage(Component.translatable("specify-suffix"));
                return;
            }

            target = player;
            suffix = args[0];
        }

        if (!target.hasPermission("suffix.unlimited.length")) {
            int maxLength = plugin.config().getInteger("suffix-length", 1);
            int suffixLength = PlainTextComponentSerializer.plainText().serialize(
                    LegacyComponentSerializer.legacyAmpersand()
                            .deserialize(suffix.replaceAll("&#([0-9a-fA-F]{6})", ""))
            ).length();
            if (maxLength < suffixLength) {
                sender.sendMessage(Component.translatable("too-long-suffix").args(Component.text(maxLength)));
                return;
            }
        }

        if (!target.hasPermission("suffix.unlimited.pattern")) {
            for (String blacklistPattern : plugin.config().getStringList("blacklist-pattern")) {
                if (suffix.contains(blacklistPattern)) {
                    sender.sendMessage(Component.translatable("blacklist-pattern").args(Component.text(blacklistPattern)));
                    return;
                }
            }
        }

        String suffixApplyCommand = plugin.platform().getSuffixSetCommand(
                target.getName(),
                plugin.config().getInteger("suffix-priority"),
                suffix.replace("&k", "")
        );

        server.dispatchCommand(server.getConsole(), suffixApplyCommand);
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
}
