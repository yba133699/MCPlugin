package ch.plugin.mcplugin.commands;

import ch.plugin.mcplugin.MCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("op")) {
            // Berechtigungsabfrage für "mcplugin.op"
            if (sender instanceof Player && !sender.hasPermission("mcplugin.op")) {
                sender.sendMessage(MCPlugin.getInstance().getPrefix() + MCPlugin.getInstance().getNoperms());
                return true;
            }

            // Prüfen, ob genug Argumente angegeben sind
            if (args.length != 1) {
                sender.sendMessage(MCPlugin.getInstance().getPrefix() + "§cBitte benutze: /op <player>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDer Spieler §7" + args[0] + " §cist nicht online.");
                return true;
            }

            if (target.isOp()) {
                sender.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDer Spieler §7" + args[0] + " §chat bereits Operator-Rechte.");
                return true;
            }

            // Operator-Rechte vergeben
            target.setOp(true);
            sender.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Der Spieler §a" + args[0] + " §7hat jetzt Operator-Rechte.");
            target.sendMessage(MCPlugin.getInstance().getPrefix() + "§aDu wurdest von §7" + sender.getName() + " §aals Operator gesetzt.");
            return true;
        }

        if (label.equalsIgnoreCase("deop")) {
            // Berechtigungsabfrage für "mcplugin.deop"
            if (sender instanceof Player && !sender.hasPermission("mcplugin.deop")) {
                sender.sendMessage(MCPlugin.getInstance().getPrefix() + MCPlugin.getInstance().getNoperms());
                return true;
            }

            // Prüfen, ob genug Argumente angegeben sind
            if (args.length != 1) {
                sender.sendMessage(MCPlugin.getInstance().getPrefix() + "§cBitte benutze: /deop <player>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDer Spieler §7" + args[0] + " §cist nicht online.");
                return true;
            }

            if (!target.isOp()) {
                sender.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDer Spieler §7" + args[0] + " §chat keine Operator-Rechte.");
                return true;
            }

            // Operator-Rechte entziehen
            target.setOp(false);
            sender.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Der Spieler §a" + args[0] + " §7hat jetzt keine Operator-Rechte mehr.");
            target.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDu wurdest von §7" + sender.getName() + " §cals Operator entfernt.");
            return true;
        }

        return false;
    }
}
