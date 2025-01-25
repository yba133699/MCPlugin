package ch.plugin.mcplugin.listener;

import ch.plugin.mcplugin.MCPlugin;
import ch.plugin.mcplugin.manager.ClanManager;
import ch.plugin.mcplugin.manager.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

        // Clan-Name abrufen
        String clanName = ClanManager.getClanTag(ClanManager.getClanIdByPlayer(p.getUniqueId()));

        // Spieler an Spawn teleportieren, falls in der Config vorhanden
        if (config.contains("spawn")) {
            String worldName = config.getString("spawn.world");
            double x = config.getDouble("spawn.x");
            double y = config.getDouble("spawn.y");
            double z = config.getDouble("spawn.z");
            float pitch = (float) config.getDouble("spawn.pitch");
            float yaw = (float) config.getDouble("spawn.yaw");

            Location spawnLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            p.teleport(spawnLocation);
        }

        // Begrüßungsnachricht setzen
        e.setJoinMessage("§7[§a+§7] " + p.getName());

        // Scoreboard mit Clan-Name setzen
        ScoreboardManager.setScoreboard(p, clanName);
    }

    @EventHandler
    public void Quit(PlayerQuitEvent e) {
        e.setQuitMessage("§7[§c-§7] " + e.getPlayer().getName());
    }
}
