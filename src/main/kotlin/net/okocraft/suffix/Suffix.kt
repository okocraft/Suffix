package net.okocraft.suffix

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.StringUtil

class Suffix : JavaPlugin(), CommandExecutor, TabExecutor {

    override fun onEnable() {
        saveDefaultConfig()
        getCommand("suffix")?.let {
            it.setExecutor(this)
            it.tabCompleter = this
        }
    }

    /**
     * /suffix player suffix
     * /suffix suffix
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (args.isEmpty()) {
            return false
        }

        if (!sender.hasPermission("suffix.change")) {
            sender.sendMessage(config.getString("messages.no-permission", "")!!)
            return false
        }

        val player: Player
        var suffix: String

        when {
            args.size > 1 -> {
                if (!sender.hasPermission("suffix.change.other")) {
                    sender.sendMessage(config.getString("messages.no-permission", "")!!)
                }

                if (Bukkit.getPlayer(args[0]) == null) {
                    sender.sendMessage(config.getString("messages.player-is-offline", "")!!)
                    return false
                }

                player = Bukkit.getPlayer(args[0])!!
                suffix = ChatColor.translateAlternateColorCodes('&', args[1])
            }
            sender is Player -> {
                player = sender
                suffix = ChatColor.translateAlternateColorCodes('&', args[0])
            }
            else -> {
                sender.sendMessage(config.getString("messages.specify-player", "")!!)
                return false
            }
        }

        val suffixChar = ChatColor.stripColor(suffix)!![0]
        suffix = suffix.substringBefore(suffixChar) + suffixChar
        val suffixCommand = config.getString("suffix-command", "")!!
            .replace("%player%", player.name)
            .replace("%suffix%", suffix)

        return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), suffixCommand)
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        if (!sender.hasPermission("suffix.change.other")) {
            val players = Bukkit.getOnlinePlayers().toList().map { it.name }.toMutableList()
            if (args.size == 1) {
                return StringUtil.copyPartialMatches(args[0], players, mutableListOf())
            }

            if (args.size == 2) {
                return mutableListOf("<suffix>")
            }

            return mutableListOf()
        }

        if (args.size == 1) {
            return mutableListOf("<suffix>")
        }

        return mutableListOf()
    }
}