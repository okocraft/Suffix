package net.okocraft.suffix.core.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Map;

public final class MessageMap {

    private final Map<String, String> back;

    public MessageMap(Map<String, String> back) {
        this.back = back;
    }

    public Component formatted(String key) {
        return MiniMessage.miniMessage().deserialize(this.getMessage(key));
    }

    public Component formatted(String key, TagResolver resolver) {
        return MiniMessage.miniMessage().deserialize(this.getMessage(key), resolver);
    }

    public Component formatted(String key, TagResolver... resolvers) {
        return MiniMessage.miniMessage().deserialize(this.getMessage(key), resolvers);
    }

    private String getMessage(String key) {
        String result = this.back.get(key);

        if (result != null) {
            return result;
        } else {
            throw new IllegalStateException("message '" + key + "' is not registered.");
        }
    }

}
