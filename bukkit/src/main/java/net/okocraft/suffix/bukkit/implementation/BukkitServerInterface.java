package net.okocraft.suffix.bukkit.implementation;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import net.okocraft.suffix.bukkit.Main;
import net.okocraft.suffix.core.api.command.sender.CommandSender;
import net.okocraft.suffix.core.api.command.sender.ConsoleCommandSender;
import net.okocraft.suffix.core.api.command.sender.Player;
import net.okocraft.suffix.core.api.ServerInterface;
import net.okocraft.suffix.core.api.command.Command;
import net.okocraft.suffix.core.api.command.TabCompleter;
import org.bukkit.command.PluginCommand;

public class BukkitServerInterface implements ServerInterface {

    private final Main plugin;

    public BukkitServerInterface(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerCommand(Command command) {
        PluginCommand pc = Objects.requireNonNull(plugin.getCommand(command.getName()));
        pc.setExecutor((sender, cmd, label, args) -> {
            command.execute(toAPISender(sender), args);
            return true;
        });
        if (command instanceof TabCompleter) {
            pc.setTabCompleter((sender, command1, label, args) -> ((TabCompleter) command).onTabComplete(toAPISender(sender), args));
        }
    }

    @Override
    public void unregisterCommand(Command command) {
        PluginCommand pc = Objects.requireNonNull(plugin.getCommand(command.getName()));
        pc.setExecutor(null);
        pc.setTabCompleter(null);
    }

    @Override
    public void dispatchCommand(CommandSender sender, String command) {
        org.bukkit.command.CommandSender bukkitCommandSender;
        if (sender instanceof Player) {
            org.bukkit.entity.Player bukkitPlayer = plugin.getServer().getPlayer(((Player) sender).getUniqueId());
            if (bukkitPlayer != null) {
                bukkitCommandSender = bukkitPlayer;
            } else {
                bukkitCommandSender = plugin.getServer().getConsoleSender();
            }
        } else {
            bukkitCommandSender = plugin.getServer().getConsoleSender();
        }
        plugin.getServer().dispatchCommand(bukkitCommandSender, command);
    }

    @Override
    public Player getPlayer(String name) {
        org.bukkit.entity.Player bukkitPlayer = plugin.getServer().getPlayer(name);
        return bukkitPlayer == null ? null : toAPIPlayer(bukkitPlayer);
    }

    @Override
    public Player getPlayer(UUID uniqueId) {
        org.bukkit.entity.Player bukkitPlayer = plugin.getServer().getPlayer(uniqueId);
        return bukkitPlayer == null ? null : toAPIPlayer(bukkitPlayer);
    }

    @Override
    public CommandSender getConsole() {
        return toAPISender(plugin.getServer().getConsoleSender());
    }

    @Override
    public Collection<Player> getPlayers() {
        return plugin.getServer().getOnlinePlayers().stream()
                .map(BukkitServerInterface::toAPIPlayer)
                .collect(Collectors.toSet());
    }

    private static Player toAPIPlayer(org.bukkit.entity.Player bukkitPlayer) {
        if (bukkitPlayer == null) {
            return null;
        }
        return new Player() {
            @Override
            public UUID getUniqueId() {
                return bukkitPlayer.getUniqueId();
            }

            @Override
            public String getName() {
                return bukkitPlayer.getName();
            }

            @Override
            public boolean hasPermission(String permission) {
                return bukkitPlayer.hasPermission(permission);
            }
        };
    }

    private static CommandSender toAPISender(org.bukkit.command.CommandSender bukkitSender) {
        if (bukkitSender == null) {
            return null;
        }

        if (bukkitSender instanceof org.bukkit.entity.Player) {
            return toAPIPlayer((org.bukkit.entity.Player) bukkitSender);
        }

        if (bukkitSender instanceof org.bukkit.command.ConsoleCommandSender) {
            return new ConsoleCommandSender() {
                @Override
                public String getName() {
                    return bukkitSender.getName();
                }

                @Override
                public boolean hasPermission(String permission) {
                    return bukkitSender.hasPermission(permission);
                }
            };
        }

        return new CommandSender() {
            @Override
            public String getName() {
                return bukkitSender.getName();
            }

            @Override
            public boolean hasPermission(String permission) {
                return bukkitSender.hasPermission(permission);
            }
        };
    }
}
