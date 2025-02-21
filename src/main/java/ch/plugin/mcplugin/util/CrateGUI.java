package ch.plugin.mcplugin.util;

import ch.plugin.mcplugin.MCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.inventory.meta.SkullMeta;


import java.util.Random;

public class CrateGUI implements Listener {

    private final MCPlugin plugin;
    private final Player player;
    private final Inventory inventory;
    private final WheelSpinItems wheelSpinItems; // Klasse für Items und Wahrscheinlichkeiten
    private int spinIndex = 0;
    private int totalTicks;
    private boolean spinning = false; // Status des Spins

    public CrateGUI(MCPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.inventory = Bukkit.createInventory(null, 54, "§6Crate Opening");
        this.wheelSpinItems = new WheelSpinItems(); // Initialisiere die Wahrscheinlichkeitsklasse

        setupGUI();
        Bukkit.getPluginManager().registerEvents(this, plugin); // Event-Registrierung
    }

    private void setupGUI() {
        // Leere das Inventar
        inventory.clear();

        // Pfeil über dem Gewinner-Slot (Slot 40)
        inventory.setItem(31, createArrowItem());
    }

    private ItemStack createArrowItem() {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setDisplayName("§6Gewinner-Anzeige");
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public void open() {
        player.openInventory(inventory);

        // Zufällige Dauer der Animation (zwischen 100 und 200 Ticks)
        totalTicks = 150;
        spinning = true; // Setze den Status auf "spinning"

        new BukkitRunnable() {
            private int ticks = 0;

            @Override
            public void run() {
                if (ticks >= totalTicks) {
                    cancel();
                    stopSpin();
                    return;
                }

                spinItems();
                playSpinSound(); // Spiele ein Geräusch bei jedem Spin
                ticks += 10; // Animation aktualisiert sich alle 10 Ticks
            }
        }.runTaskTimer(plugin, 0, 10);
    }

    private void spinItems() {
        int[] circleSlots = getCircleSlots();

        // Items im Kreis rotieren lassen, basierend auf den Wahrscheinlichkeiten
        for (int i = 0; i < circleSlots.length; i++) {
            int slot = circleSlots[i];
            ItemStack randomItem = wheelSpinItems.getRandomItem(); // Generiere ein zufälliges Item
            inventory.setItem(slot, randomItem);
        }

        spinIndex++;
    }

    private void playSpinSound() {
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.5f); // Casino-ähnlicher Klang
    }

    private void stopSpin() {
        spinning = false; // Setze den Status auf "nicht mehr spinning"

        // Slot 40 ist der Gewinner-Slot
        ItemStack winnerItem = inventory.getItem(40);

        // Überprüfe, ob der Gewinner-Slot ein Item enthält
        if (winnerItem != null) {
            player.getInventory().addItem(winnerItem); // Gewinner-Item ins Inventar legen
            player.sendMessage(MCPlugin.instance.getPrefix() + "§7Du hast gewonnen: §e" + winnerItem.getItemMeta().getDisplayName() + "!");
        } else {
            player.sendMessage("§cEs gab ein Problem mit dem Crate-System!");
        }

        // Schließe das Inventar nach 2 Sekunden
        new BukkitRunnable() {
            @Override
            public void run() {
                player.closeInventory();
            }
        }.runTaskLater(plugin, 40); // 40 Ticks = 2 Sekunden
    }

    private int[] getCircleSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16, // Oben
                25, 34,                     // Rechte Seite
                43, 42, 41, 40, 39, 38, 37, // Unten
                28, 19                      // Linke Seite
        };
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Verhindern, dass Spieler Items aus der Crate-GUI nehmen
        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Verhindere das Schließen des Inventars während des Spins
        if (spinning && event.getInventory().equals(inventory)) {
            Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(inventory));
        }
    }
}
