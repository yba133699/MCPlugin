package ch.plugin.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderChestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) {
            s.sendMessage(MCPlugin.getInstance().prefix() + "Du kannst den Befehl nicht ausführen");
            return true;
        }
        Player p = (Player) s;

        if (!p.hasPermission("mcplugin.enderchest") && !p.hasPermission("mcplugin.enderchest.other")) {
            p.sendMessage(MCPlugin.instance.prefix() + MCPlugin.getInstance().noperms());
            p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 3, 3);
            return true;
        }
        if (p.hasPermission("mcplugin.enderchest") && !p.hasPermission("mcplugin.enderchest.other")) {
            if (args.length == 0) {
                p.openInventory(p.getEnderChest());
                return true;
            }
            if (args.length > 1) {
                p.sendMessage(MCPlugin.instance.prefix() + "§cBitte benutze /enderchest <player>");
                p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 3, 3);
                return true;
            }
            p.sendMessage(MCPlugin.instance.prefix() + MCPlugin.getInstance().noperms());
            p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 3, 3);
            return true;
        }
        if (!p.hasPermission("mcplugin.enderchest") && p.hasPermission("mcplugin.enderchest.other")) {
            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    p.openInventory(target.getEnderChest());
                    return true;
                }
                p.sendMessage(MCPlugin.getInstance().prefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online!");
                p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 3, 3);
                return true;
            }
            if (args.length > 1) {
                p.sendMessage(MCPlugin.instance.prefix() + "§cBitte benutze /enderchest <player>");
                p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 3, 3);
                return true;
            }
            p.sendMessage(MCPlugin.instance.prefix() + MCPlugin.getInstance().noperms());
            p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 3, 3);
            return true;
        }
        if (args.length == 0) {
            p.openInventory(p.getEnderChest());
            return true;
        }
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                p.openInventory(target.getEnderChest());
                return true;
            }
            p.sendMessage(MCPlugin.getInstance().prefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online!");
            p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 3, 3);
            return true;
        }
        p.sendMessage(MCPlugin.instance.prefix() + "§cBitte benutze /enderchest <player>");
        p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 3, 3);
        return true;
    }
}
