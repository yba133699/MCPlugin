package ch.plugin.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class vanishCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

            HashSet<UUID> vanishedPlayers = new HashSet<>();

        if (!(s instanceof Player)) {
            s.sendMessage(MCPlugin.instance.getPrefix() + "§cNur Spieler können den Befehl ausführen");
            return true;
        }
        Player p = (Player) s;
        if (p.hasPermission("mcplugin.vanish")) {
            if (args.length == 0) {
                if (vanishedPlayers.contains(p.getUniqueId())) {
                    // Spieler ist aktuell unsichtbar, sichtbar machen
                    for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                        otherPlayer.showPlayer(p);
                    }
                    vanishedPlayers.remove(p.getUniqueId());
                    p.sendMessage("§aDu bist jetzt sichtbar!");
                } else {
                    // Spieler unsichtbar machen
                    for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                        otherPlayer.hidePlayer(p);
                    }
                    vanishedPlayers.add(p.getUniqueId());
                    p.sendMessage("§cDu bist jetzt unsichtbar!");
                }
                return true;
            }
        }
        return false;
    }
}
