package ch.plugin.mcplugin.commands;

import ch.plugin.mcplugin.MCPlugin;
import ch.plugin.mcplugin.manager.PerkManager;
import ch.plugin.mcplugin.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class PerkCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler verwendet werden.");
            return true;
        }
        Player player = (Player) sender;
        openPerkGUI(player);
        return true;
    }

    private void openPerkGUI(Player player) {
        Inventory perkGUI = Bukkit.createInventory(null, 9, ChatColor.YELLOW + "Perks Menü");

        UUID uuid = player.getUniqueId();
        boolean doubleJumpActive = PerkManager.getPerkStatus(uuid, "doublejump");
        boolean speedActive = PerkManager.getPerkStatus(uuid, "speed");
        boolean autoLootActive = PerkManager.getPerkStatus(uuid, "autoloot");
        boolean strengthActive = PerkManager.getPerkStatus(uuid, "strength");
        boolean fireResistantActive = PerkManager.getPerkStatus(uuid, "fireresistant");

        perkGUI.setItem(2, new ItemBuilder(Material.FEATHER)
                .displayname("§bDouble Jump")
                .lore(doubleJumpActive ? "§aAktiviert" : "§cDeaktiviert")
                .build());

        perkGUI.setItem(3, new ItemBuilder(Material.SUGAR)
                .displayname("§bSpeed 2")
                .lore(speedActive ? "§aAktiviert" : "§cDeaktiviert")
                .build());

        perkGUI.setItem(4, new ItemBuilder(Material.HOPPER)
                .displayname("§bAuto Loot")
                .lore(autoLootActive ? "§aAktiviert" : "§cDeaktiviert")
                .build());

        perkGUI.setItem(5, new ItemBuilder(Material.BLAZE_POWDER)
                .displayname("§bStrength 2")
                .lore(strengthActive ? "§aAktiviert" : "§cDeaktiviert")
                .build());

        perkGUI.setItem(6, new ItemBuilder(Material.MAGMA_CREAM)
                .displayname("§bFire Resistance")
                .lore(fireResistantActive ? "§aAktiviert" : "§cDeaktiviert")
                .build());

        player.openInventory(perkGUI);
    }
}
