package ch.plugin.mcplugin.manager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ChatManager implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        final String msg = e.getMessage().replace("%", "Prozent");

        String rankFormat;
        if (PermissionsEx.getUser(p).inGroup("Owner")) {
            rankFormat = "§4Owner §8» §7";
        } else if (PermissionsEx.getUser(p).inGroup("Entwickler")) {
            rankFormat = "§3Entwickler §8» §7";
        } else if (PermissionsEx.getUser(p).inGroup("Admin")) {
            rankFormat = "§cAdmin §8» §7";
        } else if (PermissionsEx.getUser(p).inGroup("Architekt")) {
            rankFormat = "§6Architekt §8» §7";
        } else if (PermissionsEx.getUser(p).inGroup("Moderator")) {
            rankFormat = "§5Moderator §8» §7";
        } else if (PermissionsEx.getUser(p).inGroup("Supporter")) {
            rankFormat = "§aSupporter §8» §7";
        } else if (PermissionsEx.getUser(p).inGroup("ProbeSupporter")) {
            rankFormat = "§aProbeSupporter §8» §7";
        } else if (PermissionsEx.getUser(p).inGroup("Emerald")) {
            rankFormat = "§aEmerald §8» §7";
        } else if (PermissionsEx.getUser(p).inGroup("Diamond")) {
            rankFormat = "§bDiamond §8» §7";
        } else if (PermissionsEx.getUser(p).inGroup("Gold")) {
            rankFormat = "§6Gold §8» §7";
        } else {
            rankFormat = "§3Spieler §8» §7";
        }

        e.setFormat(rankFormat + p.getName() + " §8» §7" + msg);
    }
}
