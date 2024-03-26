package net.okocraft.suffix.core.message;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Messages {

    private static final Map<String, String> DEFAULT_MESSAGES = new LinkedHashMap<>();

    public static final String NO_PERMISSION = def("no-permission", "<red>You do not have permission.");
    public static final String COMMAND_HELP_DEFAULT = def("help.default", "<gray>Usage: <aqua>/suffix <new suffix>");
    public static final String COMMAND_HELP_CONSOLE = def("help.console", "<gray>Usage: <aqua>/suffix <new suffix> <player>");
    public static final String COMMAND_HELP_WITH_OTHER_ARGUMENT = def("help.with-other-argument", "<gray>Usage: <aqua>/suffix <new suffix> {player}");
    public static final String PLAYER_NOT_FOUND = def("player-not-found", "<red>Player <aqua><arg><red> not found.");
    public static final String TOO_LONG_SUFFIX = def("too-long-suffix", "<aqua><arg><red> is too long suffix. Suffix should be <aqua><length> or shorter<red>.");
    public static final String BLACKLIST_PATTERN = def("blacklist-pattern", "<red>Suffix containing pattern <aqua><pattern><red> cannot be specified.");
    public static final String SUCCESS_SELF = def("success.self", "<gray>Your suffix has been updated to '<reset><suffix><gray>'.");
    public static final String SUCCESS_OTHER = def("success.other", "<aqua><player><gray>'s suffix has been updated to '<reset><suffix><gray>'.");

    private static String def(String key, String msg) {
        DEFAULT_MESSAGES.put(key, msg);
        return key;
    }

    public static Map<String, String> getDefaultMessages() {
        return DEFAULT_MESSAGES;
    }

}
