package ch.plugin.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class RespawnListener implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        FileConfiguration config = MCPlugin.getInstance().getConfig();
        if (config.contains("spawn")) {
            String worldName = config.getString("spawn.world");
            double x = config.getDouble("spawn.x");
            double y = config.getDouble("spawn.y");
            double z = config.getDouble("spawn.z");
            float pitch = (float) config.getDouble("spawn.pitch");
            float yaw = (float) config.getDouble("spawn.yaw");

            Location spawnLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            e.setRespawnLocation(spawnLocation);
        }
        p.getInventory().addItem(new ItemBuilder(Material.GOLD_SWORD).build());
        p.getInventory().setHelmet(new ItemBuilder(Material.GOLD_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).build());
        p.getInventory().setChestplate(new ItemBuilder(Material.GOLD_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).build());
        p.getInventory().setLeggings(new ItemBuilder(Material.GOLD_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).build());
        p.getInventory().setBoots(new ItemBuilder(Material.GOLD_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).build());
    }
}
