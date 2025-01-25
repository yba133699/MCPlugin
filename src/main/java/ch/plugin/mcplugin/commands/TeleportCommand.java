package ch.plugin.mcplugin.commands;

import ch.plugin.mcplugin.MCPlugin;
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
                        s.sendMessage(MCPlugin.instance.getPrefix() + "§7Du hast §a" + p1.getName() + " §7zu §a" + p2.getName() + " §7teleportiert");
                        p1.sendMessage(MCPlugin.instance.getPrefix() + "§7Du wurdest zu §a" + p2.getName() + " §7teleportiert");
                        p2.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §a" + p1.getName() + " §7wurde zu dir teleportiert");
                        return true;
                    }
                    if (p1 == null && p2 != null) {
                        s.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online");
                        return true;
                    }
                    if (p1 != null && p2 == null) {
                        s.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §c" + args[1] + " §7ist nicht online");
                        return true;
                    }
                    s.sendMessage(MCPlugin.instance.getPrefix() + "§cBeide Spieler sind nicht online");
                    return true;
                }
                s.sendMessage(MCPlugin.instance.getPrefix() + "§cBenute /tp <player> <target>");
                return true;
            }
            s.sendMessage(MCPlugin.instance.getPrefix() + "§cBenute /tp <player> <target>");
            return true;
        }

        Player p = (Player) s;
        if(label.equalsIgnoreCase("tp")) {
            if (p.hasPermission("mcplugin.tp")) {
                if (args.length == 1) {

                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        p.teleport(target.getLocation());
                        p.sendMessage(MCPlugin.instance.getPrefix() + "§7Du wurdest zu §a" + target.getName() + " §7teleportiert");
                        return true;
                    }
                    p.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online");
                    return true;
                }
                if(args.length == 0) {
                    s.sendMessage(MCPlugin.instance.getPrefix() + "§cBenute /tp <player> <target>");
                    return true;
                }
                p.sendMessage(MCPlugin.instance.getPrefix() + MCPlugin.instance.getNoperms());

            }
            if(p.hasPermission("mcplugin.tp.other")) {
                if(args.length == 2) {

                    Player p1 = Bukkit.getPlayer(args[0]);
                    Player p2 = Bukkit.getPlayer(args[1]);
                    if (p1 != null && p2 != null) {
                        p1.teleport(p2.getLocation());
                        s.sendMessage(MCPlugin.instance.getPrefix() + "§7Du hast §a" + p1.getName() + " §7zu §a" + p2.getName() + " §7teleportiert");
                        p1.sendMessage(MCPlugin.instance.getPrefix() + "§7Du wurdest zu §a" + p2.getName() + " §7teleportiert");
                        p2.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §a" + p1.getName() + " §7wurde zu dir teleportiert");
                        return true;
                    }
                    if (p1 == null && p2 != null) {
                        s.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online");
                        return true;
                    }
                    if (p1 != null && p2 == null) {
                        s.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §c" + args[1] + " §7ist nicht online");
                        return true;
                    }
                    s.sendMessage(MCPlugin.instance.getPrefix() + "§cBeide Spieler sind nicht online");
                    return true;
                }
                p.sendMessage(MCPlugin.instance.getPrefix() + "§cBenute /tp <player> <target>");
                return true;
            }
            p.sendMessage(MCPlugin.instance.getPrefix() + MCPlugin.instance.getNoperms());
            return true;


        }
        // TODO: tphere, tpa, tpahere
        return false;
    }
}
