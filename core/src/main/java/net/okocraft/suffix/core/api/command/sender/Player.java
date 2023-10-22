package net.okocraft.suffix.core.api.command.sender;

import java.util.UUID;

public abstract class Player extends CommandSender {

    public abstract UUID getUniqueId();

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Player)) {
            return false;
        }

        return getUniqueId().equals(((Player)obj).getUniqueId());
    }
}
