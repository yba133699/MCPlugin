package ch.plugin.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class OPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        switch (label.toLowerCase()) {
            case "op":
                if (sender != null) {
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            if (target.isOp()) {
                                sender.sendMessage(MCPlugin.instance.prefix() + "§cDer Spieler §7" + args[0] + " §chat schon Operator Rechte");
                                return true;
                            }
                            target.setOp(true);
                            sender.sendMessage(MCPlugin.instance.prefix() + "§7Der Spieler §a" + args[0] + " §7hat hetzt Operator Rechte");
                            return true;
                        }
                        sender.sendMessage(MCPlugin.instance.prefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online");
                        return true;
                    }
                    sender.sendMessage(MCPlugin.getInstance().prefix() + "§cBitte benutze /op <player>");
                    return true;
                }
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (target.isOp()) {
                            p.sendMessage(MCPlugin.instance.prefix() + "§cDer Spieler §7" + args[0] + " §chat schon Operator Rechte");
                            return true;
                        }
                        target.setOp(true);
                        p.sendMessage(MCPlugin.instance.prefix() + "§7Der Spieler §a" + args[0] + " §7hat hetzt Operator Rechte");
                        return true;
                    }
                    p.sendMessage(MCPlugin.instance.prefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online");
                    return true;
                }
                p.sendMessage(MCPlugin.getInstance().prefix() + "§cBitte benutze /op <player>");
                return true;

            case "deop":
                if (sender != null) {
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            if (!target.isOp()) {
                                sender.sendMessage(MCPlugin.instance.prefix() + "§cDer Spieler §7" + args[0] + " §chat keine Operator Rechte");
                                return true;
                            }
                            target.setOp(false);
                            sender.sendMessage(MCPlugin.instance.prefix() + "§7Der Spieler §a" + args[0] + " §7hat jetzt keine Operator Rechte mehr");
                            return true;
                        }
                        sender.sendMessage(MCPlugin.instance.prefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online");
                        return true;
                    }
                    sender.sendMessage(MCPlugin.getInstance().prefix() + "§cBitte benutze /op <player>");
                    return true;
                }
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (!target.isOp()) {
                            p.sendMessage(MCPlugin.instance.prefix() + "§cDer Spieler §7" + args[0] + " §chat keine Operator Rechte");
                            return true;
                        }
                        target.setOp(false);
                        p.sendMessage(MCPlugin.instance.prefix() + "§7Der Spieler §a" + args[0] + " §7hat jetzt keine Operator Rechte mehr");
                        return true;
                    }
                    p.sendMessage(MCPlugin.instance.prefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online");
                    return true;
                }
                p.sendMessage(MCPlugin.getInstance().prefix() + "§cBitte benutze /op <player>");
                return true;
        }
        return false;
    }
}
