package ch.plugin.mcplugin.listener;

import ch.plugin.mcplugin.util.ItemBuilder;
import ch.plugin.mcplugin.manager.KitCooldownManager;
import ch.plugin.mcplugin.MCPlugin;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class KitListener implements Listener {

    private final long cooldownTime = 24 * 60 * 60 * 1000; // 24 Stunden in Millisekunden
    private final FileConfiguration config = MCPlugin.getInstance().getConfig();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase("§6Wähle ein Kit:")) return;

        e.setCancelled(true);
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();

        if (clicked == null || clicked.getType() == Material.AIR) return;

        UUID playerUUID = player.getUniqueId();
        String kitName = getKitName(clicked);

        if (kitName == null) return;

        // Berechtigungsprüfung für das Kit
        if (!player.hasPermission("mcplugin.kit." + kitName.toLowerCase().replace(" kit", ""))) {
            player.sendMessage(MCPlugin.getInstance().getPrefix() + " §cDu hast keine Berechtigung für das " + kitName + ".");
            player.closeInventory();
            return;
        }

        // Cooldown prüfen
        long currentTime = System.currentTimeMillis();
        Long lastUsed = KitCooldownManager.getCooldown(playerUUID, kitName);

        if (lastUsed != null && (currentTime - lastUsed) < cooldownTime) {
            long remainingTime = cooldownTime - (currentTime - lastUsed);
            long hours = remainingTime / 1000 / 60 / 60;
            long minutes = (remainingTime / 1000 / 60) % 60;

            player.sendMessage(MCPlugin.getInstance().getPrefix() + " §cDu kannst das " + kitName + " erst in " + hours + " Stunden und " + minutes + " Minuten erneut abholen.");
            return;
        }

        // Cooldown setzen
        KitCooldownManager.setCooldown(playerUUID, kitName, currentTime);

        // Kit vergeben
        giveKit(player, kitName);
        player.sendMessage(MCPlugin.getInstance().getPrefix() + " §aDu hast erfolgreich das " + kitName + " erhalten!");
        player.closeInventory();
    }

    private String getKitName(ItemStack clicked) {
        if (clicked.getType() == Material.GOLD_INGOT) return "Gold Kit";
        if (clicked.getType() == Material.DIAMOND) return "Diamond Kit";
        if (clicked.getType() == Material.EMERALD) return "Emerald Kit";
        return null;
    }

    private void giveKit(Player player, String kitName) {
        switch (kitName) {
            case "Gold Kit":
                player.getInventory().addItem(new ItemBuilder(Material.GOLD_BLOCK).amount(1).build());
                break;

            case "Diamond Kit":
                player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_BLOCK).amount(1).build());
                break;

            case "Emerald Kit":
                player.getInventory().addItem(new ItemBuilder(Material.EMERALD_BLOCK).amount(1).build());
                break;

            default:
                break;
        }
    }
}
