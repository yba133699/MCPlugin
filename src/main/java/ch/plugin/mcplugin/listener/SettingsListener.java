package ch.plugin.mcplugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SettingsListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("Settings")) {
            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();
            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            String itemName = clickedItem.getItemMeta().getDisplayName();

            if (itemName.equals("Schönes Wetter")) {
                p.getWorld().setStorm(false);
                p.sendMessage("Wetter ist jetzt schön!");
            } else if (itemName.equals("Regen Wetter")) {
                p.getWorld().setStorm(true);
                p.sendMessage("Wetter ist jetzt regnerisch!");
            }

            p.closeInventory();
        }
    }
}
