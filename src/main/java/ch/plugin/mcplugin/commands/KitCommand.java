package ch.plugin.mcplugin.commands;

import ch.plugin.mcplugin.util.ItemBuilder;
import ch.plugin.mcplugin.MCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class KitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String Label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Du kannst kein Kit auswählen.");
            return true;
        }
        Player player = (Player) sender;
        if(args.length != 0) {
            player.sendMessage(MCPlugin.getInstance().getPrefix() + " §cBitte benutze /kit");
            return true;
        }
        Inventory kitGUI = Bukkit.createInventory(null, 3*9, "§6Wähle ein Kit:");
        kitGUI.setItem(11, new ItemBuilder(Material.GOLD_INGOT).displayname("§6Gold Kit").build());
        kitGUI.setItem(13, new ItemBuilder(Material.DIAMOND).displayname("§bDiamond Kit").build());
        kitGUI.setItem(15, new ItemBuilder(Material.EMERALD).displayname("§2Emerald Kit").build());
        player.openInventory(kitGUI);
        return true;
    }

}