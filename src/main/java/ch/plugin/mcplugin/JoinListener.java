package ch.plugin.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
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
            p.teleport(spawnLocation);
            e.setJoinMessage("§7[§a+§7] " + e.getPlayer().getName());
        }
    }
    @EventHandler
    public void Quit(PlayerQuitEvent e) {
        e.setQuitMessage("§7[§c-§7] " + e.getPlayer().getName());
    }
}