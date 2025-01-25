package ch.plugin.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SettingsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String Label, String[] args) {
        if (s instanceof Player) {
            Player p = (Player) s;
            if(p.hasPermission("mcplugin.settings")) {
                if (args.length == 0) {
                    openSettingsInventory(p);
                    return true;
                }
                p.sendMessage("§cBenutze: /settings");
                return true;
            }
            p.sendMessage(MCPlugin.instance.getPrefix() + MCPlugin.instance.getNoperms());
            return true;
        }
        return false;
    }

    private void openSettingsInventory(Player p) {
        Inventory settingsInventory = Bukkit.createInventory(null, 9, "Settings");
        ItemStack sunnyItem = new ItemStack(Material.YELLOW_FLOWER);
        ItemMeta sunnyMeta = sunnyItem.getItemMeta();
        sunnyMeta.setDisplayName("Schönes Wetter");
        sunnyItem.setItemMeta(sunnyMeta);

        ItemStack rainyItem = new ItemStack(Material.WATER_BUCKET);
        ItemMeta rainyMeta = rainyItem.getItemMeta();
        rainyMeta.setDisplayName("Regen Wetter");
        rainyItem.setItemMeta(rainyMeta);

        settingsInventory.setItem(3, sunnyItem);
        settingsInventory.setItem(5, rainyItem);

        p.openInventory(settingsInventory);
    }
}
