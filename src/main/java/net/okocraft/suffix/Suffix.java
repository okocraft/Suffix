package net.okocraft.suffix;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

public class Suffix extends JavaPlugin {

	private FileConfiguration config;
	private FileConfiguration defaultConfig;

	@Override
	public void onEnable() {
		PluginCommand command = Objects.requireNonNull(getCommand("suffix"));
		command.setExecutor(this);
		command.setTabCompleter(this);
		saveDefaultConfig();

		config = getConfig();
		defaultConfig = getDefaultConfig();
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String suffix;
		Player target;

		if (sender.hasPermission("suffix.other")) {
			if (args.length == 0) {
				sender.sendMessage(getMessage("specify-player"));
				return false;
			}

			if (args.length == 1) {
				sender.sendMessage(getMessage("specify-suffix"));
				return false;
			}

			target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage(getMessage("player-not-found"));
				return false;
			}

			suffix = args[1];

		} else {
			if (!(sender instanceof Player)) {
				sender.sendMessage(getMessage("player-only"));
				return false;
			}

			if (args.length == 0) {
				sender.sendMessage(getMessage("specify-suffix"));
				return false;
			}

			target = (Player) sender;
			suffix = args[0];
		}

		int maxLength = config.getInt("suffix-length", 1);
		int suffixLength = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', suffix)).length();
		if (maxLength < suffixLength) {
			sender.sendMessage(getMessageReplaced("too-long-suffix", Map.of("%length%", String.valueOf(maxLength))));
			return false;
		}
		for (String blacklistPattern : config.getStringList("blacklist-pattern")) {
			if (suffix.contains(blacklistPattern)) {
				sender.sendMessage(getMessageReplaced("blacklist-pattern", Map.of("%pattern%", blacklistPattern)));
				return false;
			}
		}

		String sufixApplyCommand = config
				.getString("suffix-apply-command", "lp user %player_name% meta setsuffix 10 %suffix%")
				.replaceAll("%suffix%", suffix).replaceAll("%player_name%", target.getName());

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), sufixApplyCommand);

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (sender.hasPermission("suffix.other")) {
			if (args.length == 1) {
				return StringUtil.copyPartialMatches(args[0],
						Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()),
						new ArrayList<>());
			}

			if (args.length == 2) {
				return StringUtil.copyPartialMatches(args[1], List.of("<suffix>"), new ArrayList<>());
			}
		} else {
			if (args.length == 1) {
				return StringUtil.copyPartialMatches(args[0], List.of("<suffix>"), new ArrayList<>());
			}
		}

		return List.of();
	}

	private FileConfiguration getDefaultConfig() {
		InputStream is = getResource("config.yml");
		return YamlConfiguration.loadConfiguration(new InputStreamReader(is));
	}

	private String getMessage(String key) {
		String fullKey = "messages." + key;
		return ChatColor.translateAlternateColorCodes('&', config.getString(fullKey, defaultConfig.getString(fullKey)));
	}

	private String getMessageReplaced(String key, Map<String, String> placeholders) {
		String result = getMessage(key);
		for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
			result = result.replaceAll(placeholder.getKey(), placeholder.getValue());
		}

		return result;
	}
}
