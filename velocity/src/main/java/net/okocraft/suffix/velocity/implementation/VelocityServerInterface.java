package net.okocraft.suffix.velocity.implementation;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.okocraft.suffix.velocity.Main;
import net.okocraft.suffix.core.api.ServerInterface;
import net.okocraft.suffix.core.api.command.Command;
import net.okocraft.suffix.core.api.command.TabCompleter;
import net.okocraft.suffix.core.api.command.sender.CommandSender;
import net.okocraft.suffix.core.api.command.sender.ConsoleCommandSender;
import net.okocraft.suffix.core.api.command.sender.Player;

public class VelocityServerInterface implements ServerInterface {

    private final Main plugin;
    private final ProxyServer proxy;

    private final Map<Command, CommandMeta> commandMap = new HashMap<>();

    public VelocityServerInterface(Main plugin) {
        this.plugin = plugin;
        this.proxy = plugin.proxy();
    }

    @Override
    public void registerCommand(Command command) {
        TabExecutingCommand velocityCommand;

        if (command instanceof TabCompleter) {
            TabCompleter tab = ((TabCompleter) command);
            velocityCommand = new TabExecutingCommand(command.getName()) {
                @Override
                public void execute(Invocation invocation) {
                    command.execute(toAPISender(invocation.source()), invocation.arguments());
                }

                @Override
                public List<String> suggest(Invocation invocation) {
                    return tab.onTabComplete(toAPISender(invocation.source()), invocation.arguments());
                }
            };
        } else {
            velocityCommand = new TabExecutingCommand(command.getName()) {
                @Override
                public void execute(Invocation invocation) {
                    command.execute(toAPISender(invocation.source()), invocation.arguments());
                }
            };
        }

        CommandManager commandManager = proxy.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder(velocityCommand.name())
                .plugin(plugin)
                .build();
        commandMap.put(command, commandMeta);
        commandManager.register(commandMeta, velocityCommand);
    }

    @Override
    public void unregisterCommand(Command command) {
        proxy.getCommandManager().unregister(commandMap.get(command));
    }

    @Override
    public void dispatchCommand(CommandSender sender, String command) {
        CommandSource velocitySender;
        if (sender instanceof Player) {
            Optional<com.velocitypowered.api.proxy.Player> velocityPlayer = proxy.getPlayer(((Player) sender).getUniqueId());
            if (velocityPlayer.isPresent()) {
                velocitySender = velocityPlayer.get();
            } else {
                velocitySender = proxy.getConsoleCommandSource();
            }
        } else {
            velocitySender = proxy.getConsoleCommandSource();
        }
        proxy.getCommandManager().executeImmediatelyAsync(velocitySender, command);
    }

    @Override
    public Player getPlayer(String name) {
        Optional<com.velocitypowered.api.proxy.Player> velocityPlayer = proxy.getPlayer(name);
        return velocityPlayer.map(this::toAPIPlayer).orElse(null);
    }

    @Override
    public Player getPlayer(UUID uniqueId) {
        Optional<com.velocitypowered.api.proxy.Player> velocityPlayer = proxy.getPlayer(uniqueId);
        return velocityPlayer.map(this::toAPIPlayer).orElse(null);
    }

    @Override
    public CommandSender getConsole() {
        return toAPISender(proxy.getConsoleCommandSource());
    }

    @Override
    public Collection<Player> getPlayers() {
        return proxy.getAllPlayers().stream()
                .map(this::toAPIPlayer)
                .collect(Collectors.toSet());
    }

    private Player toAPIPlayer(com.velocitypowered.api.proxy.Player velocityPlayer) {
        if (velocityPlayer == null) {
            return null;
        }
        return new Player() {

            @Override
            public UUID getUniqueId() {
                return velocityPlayer.getUniqueId();
            }

            @Override
            public String getName() {
                return velocityPlayer.getUsername();
            }

            @Override
            public boolean hasPermission(String permission) {
                return velocityPlayer.hasPermission(permission);
            }

            @Override
            public Locale getLocale() {
                return velocityPlayer.getEffectiveLocale();
            }

            @Override
            public void sendMessage(Component message) {
                velocityPlayer.sendMessage(message);
            }
        };
    }

    private CommandSender toAPISender(CommandSource velocitySender) {
        if (velocitySender == null) {
            return null;
        }

        if (velocitySender instanceof com.velocitypowered.api.proxy.Player) {
            return toAPIPlayer((com.velocitypowered.api.proxy.Player) velocitySender);
        }

        if (velocitySender instanceof ConsoleCommandSource) {
            return new ConsoleCommandSender() {
                @Override
                public String getName() {
                    return "CONSOLE";
                }

                @Override
                public boolean hasPermission(String permission) {
                    return velocitySender.hasPermission(permission);
                }

                @Override
                public void sendMessage(Component message) {
                    proxy.getConsoleCommandSource().sendMessage(message);
                }
            };
        }

        // unreachable.
        return new CommandSender() {

            public String getName() {
                return "null";
            }

            @Override
            public boolean hasPermission(String permission) {
                return velocitySender.hasPermission(permission);
            }

            @Override
            public void sendMessage(Component message) {
                velocitySender.sendMessage(message);
            }
        };
    }
}
