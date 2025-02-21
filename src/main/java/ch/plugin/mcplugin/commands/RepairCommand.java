package ch.plugin.mcplugin.commands;

import ch.plugin.mcplugin.MCPlugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RepairCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MCPlugin.getInstance().getPrefix() + "§cNur Spieler können diesen Befehl verwenden.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("mcplugin.repair")) {
            player.sendMessage(MCPlugin.getInstance().getPrefix() + MCPlugin.getInstance().getNoperms());
            return true;
        }

        if (args.length == 0) {
            // Repariere das Item in der Hand
            ItemStack itemInHand = player.getItemInHand();

            if (itemInHand == null || itemInHand.getType() == Material.AIR) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDu hältst kein reparierbares Item.");
                return true;
            }

            if (!isRepairable(itemInHand.getType())) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDieses Item kann nicht repariert werden.");
                return true;
            }

            itemInHand.setDurability((short) 0); // Setze die Haltbarkeit des Items auf 0
            player.updateInventory(); // Aktualisiere das Inventar des Spielers
            player.sendMessage(MCPlugin.getInstance().getPrefix() + "§aDas Item in deiner Hand wurde repariert!");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("all")) {
            if (!player.hasPermission("mcplugin.repair.all")) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + MCPlugin.getInstance().getNoperms());
                return true;
            }

            boolean repairedAny = false;

            // Repariere Items im Inventar
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && isRepairable(item.getType()) && item.getDurability() > 0) {
                    item.setDurability((short) 0);
                    repairedAny = true;
                }
            }

            // Repariere Rüstungsteile
            for (ItemStack armorItem : player.getInventory().getArmorContents()) {
                if (armorItem != null && isRepairable(armorItem.getType()) && armorItem.getDurability() > 0) {
                    armorItem.setDurability((short) 0);
                    repairedAny = true;
                }
            }

            player.updateInventory(); // Aktualisiere das Inventar des Spielers

            if (repairedAny) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§aAlle Items in deinem Inventar wurden repariert!");
            } else {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cKeine reparierbaren Items in deinem Inventar gefunden.");
            }

            return true;
        }

        // Wenn die Argumente ungültig sind
        player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cVerwendung: /repair oder /repair all");
        return true;
    }

    private boolean isRepairable(Material material) {
        // Liste der reparierbaren Materialien (für ältere Versionen)
        return material == Material.WOOD_SWORD || material == Material.STONE_SWORD ||
                material == Material.IRON_SWORD || material == Material.GOLD_SWORD ||
                material == Material.DIAMOND_SWORD || material == Material.WOOD_AXE ||
                material == Material.STONE_AXE || material == Material.IRON_AXE ||
                material == Material.GOLD_AXE || material == Material.DIAMOND_AXE ||
                material == Material.WOOD_PICKAXE || material == Material.STONE_PICKAXE ||
                material == Material.IRON_PICKAXE || material == Material.GOLD_PICKAXE ||
                material == Material.DIAMOND_PICKAXE || material == Material.WOOD_SPADE ||
                material == Material.STONE_SPADE || material == Material.IRON_SPADE ||
                material == Material.GOLD_SPADE || material == Material.DIAMOND_SPADE ||
                material == Material.WOOD_HOE || material == Material.STONE_HOE ||
                material == Material.IRON_HOE || material == Material.GOLD_HOE ||
                material == Material.DIAMOND_HOE || material == Material.LEATHER_HELMET ||
                material == Material.CHAINMAIL_HELMET || material == Material.IRON_HELMET ||
                material == Material.GOLD_HELMET || material == Material.DIAMOND_HELMET ||
                material == Material.LEATHER_CHESTPLATE || material == Material.CHAINMAIL_CHESTPLATE ||
                material == Material.IRON_CHESTPLATE || material == Material.GOLD_CHESTPLATE ||
                material == Material.DIAMOND_CHESTPLATE || material == Material.LEATHER_LEGGINGS ||
                material == Material.CHAINMAIL_LEGGINGS || material == Material.IRON_LEGGINGS ||
                material == Material.GOLD_LEGGINGS || material == Material.DIAMOND_LEGGINGS ||
                material == Material.LEATHER_BOOTS || material == Material.CHAINMAIL_BOOTS ||
                material == Material.IRON_BOOTS || material == Material.GOLD_BOOTS ||
                material == Material.DIAMOND_BOOTS || material == Material.FISHING_ROD ||
                material == Material.FLINT_AND_STEEL || material == Material.BOW;
    }
}
