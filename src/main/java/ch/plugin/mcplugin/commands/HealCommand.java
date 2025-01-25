package ch.plugin.mcplugin.commands;

import ch.plugin.mcplugin.MCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class HealCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length != 1) {
                sender.sendMessage("§cAls KONSOLE kannst du nur andere Spieler heilen /heal <SPIELER>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                target.setHealth(target.getMaxHealth());
                target.setFoodLevel(20);
                target.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du wurdest von §aCONSOLE §7geheilt!");
                target.playSound(target.getLocation(), Sound.LEVEL_UP, 4, 4);
                sender.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du hast §a" + target.getName() + " §7geheilt!");
                return true;
            }
            sender.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online!");
            return true;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("mcplugin.heal") && !player.hasPermission("mcplugin.heal.other")) {
            player.sendMessage(MCPlugin.getInstance().getPrefix() + MCPlugin.getInstance().getNoperms());
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 3, 3);
            return true;
        }
        if(!player.hasPermission("mcplugin.heal") && player.hasPermission("mcplugin.heal.other")) {
            if (args.length != 1) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + MCPlugin.getInstance().getNoperms());
                player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 3, 3);
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if(target != null) {
                target.setHealth(target.getMaxHealth());
                target.setFoodLevel(20);
                target.sendMessage(MCPlugin.getInstance().getPrefix() + " §7Du wurdest von §a" + player.getName() + "§7geheilt!");
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du hast §a" + target.getName() + " §7geheilt!");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 4, 4);
                return true;
            }
            player.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online!");
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 3, 3);
            return true;
        }
        if(player.hasPermission("mcplugin.heal") && !player.hasPermission("mcplugin.heal.other")) {
            if (args.length > 1) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cBitte benutze /heal <SPIELER>");
                player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 3, 3);
                return true;
            }
            if (args.length != 1) {
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du hast §adich §7geheilt!");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 4, 4);
                return true;
            }
            player.sendMessage(MCPlugin.getInstance().getPrefix() + MCPlugin.getInstance().getNoperms());
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 3, 3);
            return true;
        }
        if(player.hasPermission("mcplugin.heal") && player.hasPermission("mcplugin.heal.other")) {
            if (args.length > 1) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cBitte benutze /heal <player>");
                player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 3, 3);
                return true;
            }
            if (args.length != 1) {
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du hast §adich §7geheilt!");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 4, 4);
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if(target != null) {
                target.setHealth(target.getMaxHealth());
                target.setFoodLevel(20);
                target.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du wurdest von §a" + player.getName() + " §7geheilt!");
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du hast §a" + target.getName() + " §7geheilt!");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 4, 4);
                return true;
            }
            player.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online!");
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 3, 3);
            return true;
        }
        return false;
    }
}
