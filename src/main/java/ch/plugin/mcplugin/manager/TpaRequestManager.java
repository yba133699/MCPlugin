package ch.plugin.mcplugin.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TpaRequestManager {

    private static final HashMap<UUID, Request> tpaRequests = new HashMap<>();

    public static void addRequest(UUID requester, UUID target, boolean isTpaHere) {
        tpaRequests.put(target, new Request(requester, isTpaHere));
    }

    public static Player getRequester(UUID target) {
        Request request = tpaRequests.get(target);
        return request != null ? Bukkit.getPlayer(request.getRequester()) : null;
    }

    public static boolean isTpaHere(UUID target) {
        Request request = tpaRequests.get(target);
        return request != null && request.isTpaHere();
    }

    public static void removeRequest(UUID target) {
        tpaRequests.remove(target);
    }

    private static class Request {
        private final UUID requester;
        private final boolean isTpaHere;

        public Request(UUID requester, boolean isTpaHere) {
            this.requester = requester;
            this.isTpaHere = isTpaHere;
        }

        public UUID getRequester() {
            return requester;
        }

        public boolean isTpaHere() {
            return isTpaHere;
        }
    }
}
