package ch.plugin.mcplugin.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldProtectionListener implements Listener {

    private final String protectedWorldName;

    public WorldProtectionListener(String protectedWorldName) {
        this.protectedWorldName = protectedWorldName; // Die Welt, die geschützt werden soll
    }

//    @EventHandler
//    public void onMobSpawn(CreatureSpawnEvent event) {
//        World world = event.getLocation().getWorld();
//        if (world != null && world.getName().equalsIgnoreCase(protectedWorldName)) {
//            event.setCancelled(true); // Verhindert das Spawnen von Mobs
//        }
//    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        World world = event.getWorld();
        if (world.getName().equalsIgnoreCase(protectedWorldName)) {
            event.setCancelled(true); // Verhindert Wetterwechsel
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (world.getName().equalsIgnoreCase(protectedWorldName)) {
            if (!player.hasPermission("mcplugin.bypassblockbreak")) { // Erlaubt Admins mit dieser Permission das Abbauen
                event.setCancelled(true); // Verhindert das Abbauen von Blöcken
            }
        }
    }
}
