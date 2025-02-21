package ch.plugin.mcplugin.listener;

import ch.plugin.mcplugin.manager.CombatManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class CombatListener implements Listener {

    private final CombatManager combatManager;
    private final Location spawnLocation;

    public CombatListener(CombatManager combatManager) {
        this.combatManager = combatManager;
        if (Bukkit.getWorld("world") != null) {
            this.spawnLocation = Bukkit.getWorld("world").getSpawnLocation();
        } else {
            this.spawnLocation = null;
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof Player /*&& event.getDamager() instanceof Player*/) {
            Player player = (Player) event.getEntity();
            //Player damager = (Player) event.getDamager();
            if (!combatManager.isInCombat(player)) {
                combatManager.startCombatCooldown(player);
                //combatManager.startCombatCooldown(damager);
            }

        }
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (spawnLocation == null) {
            return;
        }

        if (combatManager.isInCombat(player) && event.getTo().distance(spawnLocation) < 25) {
            event.setCancelled(true);
            player.sendMessage("§cDu kannst den Spawn nicht betreten, während du im Kampf bist!");
        }
    }
}
