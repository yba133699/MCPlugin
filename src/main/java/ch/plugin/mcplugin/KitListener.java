package ch.plugin.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class KitListener implements Listener {

    // Cooldowns: Spieler -> (Kit-Name -> Letzte Zeit)
    private final HashMap<UUID, HashMap<String, Long>> cooldowns = new HashMap<>();
    private final long cooldownTime = 24 * 60 * 60 * 1000; // 24 Stunden in Millisekunden
    private final FileConfiguration config = MCPlugin.getInstance().getConfig();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("§6Wähle ein Kit:")) {
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
                player.sendMessage(MCPlugin.getInstance().prefix() + " §cDu hast keine Berechtigung für das " + kitName + ".");
                player.closeInventory();
                return;
            }

            // Cooldown prüfen
            long currentTime = System.currentTimeMillis();
            if (cooldowns.containsKey(playerUUID)) {
                HashMap<String, Long> playerCooldowns = cooldowns.get(playerUUID);
                if (playerCooldowns.containsKey(kitName)) {
                    long lastUsed = playerCooldowns.get(kitName);
                    if (currentTime - lastUsed < cooldownTime) {
                        long remainingTime = cooldownTime - (currentTime - lastUsed);
                        long hours = remainingTime / 1000 / 60 / 60;
                        player.sendMessage(MCPlugin.getInstance().prefix() + " §cDu kannst das " + kitName + " erst in " + hours + " Stunden erneut abholen.");
                        return;
                    }
                }
            }

            // Cooldown setzen
            cooldowns.putIfAbsent(playerUUID, new HashMap<>());
            cooldowns.get(playerUUID).put(kitName, currentTime);

            // Kit vergeben
            giveKit(player, kitName);
            player.sendMessage(MCPlugin.getInstance().prefix() + " §aDu hast erfolgreich das " + kitName + " erhalten!");
            player.closeInventory();
        }
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
