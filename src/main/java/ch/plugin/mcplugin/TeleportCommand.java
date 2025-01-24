package ch.plugin.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) {
            if(label.equalsIgnoreCase("tp")) {
                if (args.length == 2) {
                    Player p1 = Bukkit.getPlayer(args[0]);
                    Player p2 = Bukkit.getPlayer(args[1]);
                    if (p1 != null && p2 != null) {
                        p1.teleport(p2.getLocation());
                        s.sendMessage(MCPlugin.instance.prefix() + "§7Du hast §a" + p1.getName() + " §7zu §a" + p2.getName() + " §7teleportiert");
                        p1.sendMessage(MCPlugin.instance.prefix() + "§7Du wurdest zu §a" + p2.getName() + " §7teleportiert");
                        p2.sendMessage(MCPlugin.instance.prefix() + "§7Der Spieler §a" + p1.getName() + " §7wurde zu dir teleportiert");
                        return true;
                    }
                    if (p1 == null && p2 != null) {
                        s.sendMessage(MCPlugin.instance.prefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online");
                        return true;
                    }
                    if (p1 != null && p2 == null) {
                        s.sendMessage(MCPlugin.instance.prefix() + "§7Der Spieler §c" + args[1] + " §7ist nicht online");
                        return true;
                    }
                    s.sendMessage(MCPlugin.instance.prefix() + "§cBeide Spieler sind nicht online");
                    return true;
                }
                s.sendMessage(MCPlugin.instance.prefix() + "§cBenute /tp <player> <target>");
                return true;
            }
            s.sendMessage(MCPlugin.instance.prefix() + "§cBenute /tp <player> <target>");
            return true;
        }
        // TODO
        return false;
    }
}
