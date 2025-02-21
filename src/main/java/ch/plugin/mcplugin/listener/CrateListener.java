package ch.plugin.mcplugin.listener;

import ch.plugin.mcplugin.MCPlugin;
import ch.plugin.mcplugin.util.CrateGUI;
import ch.plugin.mcplugin.util.WheelSpinItems;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CrateListener implements Listener {

    private final MCPlugin plugin;
    private final WheelSpinItems wheelSpinItems;

    public CrateListener(MCPlugin plugin) {
        this.plugin = plugin;
        this.wheelSpinItems = new WheelSpinItems();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        // Überprüfen, ob der Block ein Spieler-Kopf ist
        if (block != null && block.getType() == Material.SKULL) {
            BlockState state = block.getState();
            if (state instanceof Skull) {
                Skull skull = (Skull) state;

                // Prüfen, ob es ein Spieler-Kopf ist
                if (skull.getSkullType() == SkullType.PLAYER) {
                    ItemStack item = player.getItemInHand();

                    // Linksklick: Zeige mögliche Gewinn-Items an
                    if (event.getAction().toString().contains("LEFT_CLICK") &&
                            item != null && item.getType() == Material.BLAZE_ROD) {
                        showPossibleRewards(player);
                        return;
                    }

                    // Rechtsklick: Öffne die Crate
                    if (event.getAction().toString().contains("RIGHT_CLICK") &&
                            item != null && item.getType() == Material.BLAZE_ROD) {
                        // Crate Key entfernen
                        item.setAmount(item.getAmount() - 1);

                        // Crate GUI öffnen
                        new CrateGUI(plugin, player).open();
                    } else {
                        player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu benötigst einen §6Crate Key§c, um diese Kiste zu öffnen!");
                    }
                }
            }
        }
    }

    /**
     * Zeigt ein Inventar mit allen möglichen Gewinn-Items an.
     *
     * @param player Der Spieler, der die Vorschau sehen möchte.
     */
    private void showPossibleRewards(Player player) {
        Inventory previewInventory = plugin.getServer().createInventory(null, 54, "§6Mögliche Gewinne");

        // Alle möglichen Items aus der WheelSpinItems-Klasse hinzufügen
        wheelSpinItems.getItems().forEach(spinItem -> {
            previewInventory.addItem(spinItem.getItemStack());
        });

        // Öffne das Vorschauinventar
        player.openInventory(previewInventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Blockiere das Entfernen von Items aus dem Crate-Vorschau-Inventar
        if (event.getView().getTitle().equals("§6Mögliche Gewinne")) {
            event.setCancelled(true);
        }
    }
}
