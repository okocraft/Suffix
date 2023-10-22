package net.okocraft.suffix.bungeecord.implementation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.okocraft.suffix.bungeecord.Main;
import net.okocraft.suffix.core.api.ServerInterface;
import net.okocraft.suffix.core.api.command.Command;
import net.okocraft.suffix.core.api.command.TabCompleter;
import net.okocraft.suffix.core.api.command.sender.CommandSender;
import net.okocraft.suffix.core.api.command.sender.ConsoleCommandSender;
import net.okocraft.suffix.core.api.command.sender.Player;

public class BungeeServerInterface implements ServerInterface {

    private final Main plugin;

    private final Map<Command, net.md_5.bungee.api.plugin.Command> commandMap = new HashMap<>();

    public BungeeServerInterface(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerCommand(Command command) {
        net.md_5.bungee.api.plugin.Command bungeeCommand;

        if (command instanceof TabCompleter) {
            TabCompleter tab = ((TabCompleter) command);
            bungeeCommand = new TabExecutingCommand(command.getName()) {
                @Override
                public void execute(net.md_5.bungee.api.CommandSender sender, String[] args) {
                    command.execute(toAPISender(sender), args);
                }

                @Override
                public Iterable<String> onTabComplete(net.md_5.bungee.api.CommandSender sender, String[] args) {
                    return tab.onTabComplete(toAPISender(sender), args);
                }
            };
        } else {
            bungeeCommand = new net.md_5.bungee.api.plugin.Command(command.getName()) {
                @Override
                public void execute(net.md_5.bungee.api.CommandSender sender, String[] args) {
                    command.execute(toAPISender(sender), args);
                }
            };
        }

        commandMap.put(command, bungeeCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(plugin, bungeeCommand);
    }

    @Override
    public void unregisterCommand(Command command) {
        net.md_5.bungee.api.plugin.Command bungeeCommand = commandMap.get(command);
        ProxyServer.getInstance().getPluginManager().unregisterCommand(bungeeCommand);
    }

    @Override
    public void dispatchCommand(CommandSender sender, String command) {
        net.md_5.bungee.api.CommandSender bungeeSender;
        if (sender instanceof Player) {
            ProxiedPlayer bungeePlayer = ProxyServer.getInstance().getPlayer(((Player) sender).getUniqueId());
            if (bungeePlayer != null) {
                bungeeSender = bungeePlayer;
            } else {
                bungeeSender = ProxyServer.getInstance().getConsole();
            }
        } else {
            bungeeSender = ProxyServer.getInstance().getConsole();
        }
        ProxyServer.getInstance().getPluginManager().dispatchCommand(bungeeSender, command);
    }

    @Override
    public Player getPlayer(String name) {
        ProxiedPlayer bungeePlayer = ProxyServer.getInstance().getPlayer(name);
        return bungeePlayer == null ? null : toAPIPlayer(bungeePlayer);
    }

    @Override
    public Player getPlayer(UUID uniqueId) {
        ProxiedPlayer bungeePlayer = ProxyServer.getInstance().getPlayer(uniqueId);
        return bungeePlayer == null ? null : toAPIPlayer(bungeePlayer);
    }

    @Override
    public CommandSender getConsole() {
        return toAPISender(ProxyServer.getInstance().getConsole());
    }

    @Override
    public Collection<Player> getPlayers() {
        return ProxyServer.getInstance().getPlayers().stream()
                .map(this::toAPIPlayer)
                .collect(Collectors.toSet());
    }

    private Player toAPIPlayer(ProxiedPlayer bungeePlayer) {
        if (bungeePlayer == null) {
            return null;
        }
        return new Player() {

            private final Audience player = plugin.adventure().player(bungeePlayer);

            @Override
            public UUID getUniqueId() {
                return bungeePlayer.getUniqueId();
            }

            @Override
            public String getName() {
                return bungeePlayer.getName();
            }

            @Override
            public boolean hasPermission(String permission) {
                return bungeePlayer.hasPermission(permission);
            }

            @Override
            public void sendMessage(Component message) {
                player.sendMessage(message);
            }
        };
    }

    private CommandSender toAPISender(net.md_5.bungee.api.CommandSender bungeeSender) {
        if (bungeeSender == null) {
            return null;
        }

        if (bungeeSender instanceof ProxiedPlayer) {
            return toAPIPlayer((ProxiedPlayer) bungeeSender);
        }

        if (bungeeSender.equals(ProxyServer.getInstance().getConsole())) {
            return new ConsoleCommandSender() {
                @Override
                public String getName() {
                    return bungeeSender.getName();
                }

                @Override
                public boolean hasPermission(String permission) {
                    return bungeeSender.hasPermission(permission);
                }

                @Override
                public void sendMessage(Component message) {
                    plugin.adventure().console().sendMessage(message);
                }
            };
        }

        return new CommandSender() {

            private final Audience sender = plugin.adventure().sender(bungeeSender);
            @Override
            public String getName() {
                return bungeeSender.getName();
            }

            @Override
            public boolean hasPermission(String permission) {
                return bungeeSender.hasPermission(permission);
            }

            @Override
            public void sendMessage(Component message) {
                sender.sendMessage(message);
            }
        };
    }
}
