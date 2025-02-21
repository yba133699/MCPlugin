package ch.plugin.mcplugin.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ch.plugin.mcplugin.MCPlugin;

import java.util.HashMap;
import java.util.UUID;

public class CombatManager implements Listener {

    private final HashMap<UUID, Long> combatPlayers = new HashMap<>();
    private final long combatDuration = 10000; // 10 Sekunden Kampfzeit
    private final Location spawnLocation = Bukkit.getWorld("world").getSpawnLocation();

    public void setInCombat(Player player) {
        combatPlayers.put(player.getUniqueId(), System.currentTimeMillis());
        player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu bist jetzt im Kampf!");
    }

    public boolean isInCombat(Player player) {
        return combatPlayers.containsKey(player.getUniqueId()) &&
                (System.currentTimeMillis() - combatPlayers.get(player.getUniqueId()) < combatDuration);
    }

    public void removeFromCombat(Player player) {
        combatPlayers.remove(player.getUniqueId());
        player.sendMessage(MCPlugin.instance.getPrefix() + "§aDu bist jetzt nicht mehr im Kampf!");
    }

    public void startCombatCooldown(Player player) {
        setInCombat(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isInCombat(player)) {
                    cancel();
                }
                removeFromCombat(player);
            }
        }.runTaskLater(MCPlugin.getInstance(), combatDuration / 50);
    }
}
