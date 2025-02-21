package ch.plugin.mcplugin.commands;

import ch.plugin.mcplugin.MCPlugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class StackCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MCPlugin.getInstance().getPrefix() + "§cNur Spieler können diesen Befehl verwenden.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("mcplugin.stack")) {
            player.sendMessage(MCPlugin.getInstance().getPrefix() + MCPlugin.getInstance().getNoperms());
            return true;
        }

        ItemStack[] contents = player.getInventory().getContents();
        Map<ItemStack, Integer> itemCounts = new HashMap<>();
        Map<ItemStack, Integer> highestSlotMap = new HashMap<>();

        // Schritt 1: Gesamtanzahl jedes Items berechnen und höchsten Slot speichern
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.getType() != Material.AIR) {
                boolean found = false;
                for (ItemStack key : itemCounts.keySet()) {
                    if (key.isSimilar(item)) {
                        itemCounts.put(key, itemCounts.get(key) + item.getAmount());
                        if (i > highestSlotMap.get(key)) {
                            highestSlotMap.put(key, i); // Höchsten Slot aktualisieren
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    itemCounts.put(item.clone(), item.getAmount());
                    highestSlotMap.put(item.clone(), i);
                }
            }
        }

        // Schritt 2: Items auf den höchsten Slot zusammenführen
        for (Map.Entry<ItemStack, Integer> entry : itemCounts.entrySet()) {
            ItemStack baseItem = entry.getKey();
            int totalAmount = entry.getValue();
            int maxStackSize = baseItem.getType().getMaxStackSize();
            int highestSlotIndex = highestSlotMap.get(baseItem);

            // Setze die gestapelte Menge auf den höchsten Slot
            contents[highestSlotIndex] = baseItem.clone();
            contents[highestSlotIndex].setAmount(Math.min(totalAmount, maxStackSize));
            totalAmount -= contents[highestSlotIndex].getAmount();

            // Leere die übrigen Slots dieses Item-Typs
            for (int i = 0; i < contents.length; i++) {
                ItemStack item = contents[i];
                if (item != null && item.isSimilar(baseItem) && i != highestSlotIndex) {
                    if (totalAmount > 0) {
                        item.setAmount(Math.min(totalAmount, maxStackSize));
                        totalAmount -= item.getAmount();
                    } else {
                        contents[i] = null;
                    }
                }
            }
        }

        // Inventar aktualisieren
        player.getInventory().setContents(contents);
        player.sendMessage(MCPlugin.getInstance().getPrefix() + "§aAlle Items wurden entsprechend gestapelt!");

        return true;
    }
}
