package ch.plugin.mcplugin.listener;

import ch.plugin.mcplugin.MCPlugin;
import ch.plugin.mcplugin.manager.PerkManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PerkListener implements Listener {

    private final Set<UUID> doubleJumpUsed = new HashSet<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PerkManager.loadPerks(event.getPlayer());
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        // 5 Ticks warten, damit Minecraft den Spieler vollständig gespawnt hat
        Bukkit.getScheduler().runTaskLater(MCPlugin.getInstance(), () -> PerkManager.loadPerks(player), 5L);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!event.getView().getTitle().equals(ChatColor.YELLOW + "Perks Menü")) return;

        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;

        Material itemType = clickedItem.getType();
        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null) return;

        UUID uuid = player.getUniqueId();
        boolean newState;
        String perkName = "";
        String permission;

        switch (itemType) {
            case FEATHER:
                permission = "perk.doublejump";
                if (!player.hasPermission(permission)) {
                    player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDu hast keine Berechtigung für §eDouble Jump§c!");
                    return;
                }
                newState = !PerkManager.getPerkStatus(uuid, "doublejump");
                PerkManager.setPerkStatus(uuid, "doublejump", newState);
                player.setAllowFlight(newState);
                perkName = "Double Jump";
                if (!newState) {
                    doubleJumpUsed.remove(uuid);
                    player.setFlying(false);
                }
                break;
            case SUGAR:
                permission = "perk.speed";
                if (!player.hasPermission(permission)) {
                    player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDu hast keine Berechtigung für §eSpeed§c!");
                    return;
                }
                newState = !PerkManager.getPerkStatus(uuid, "speed");
                PerkManager.setPerkStatus(uuid, "speed", newState);
                perkName = "Speed";
                if (newState) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
                } else {
                    player.removePotionEffect(PotionEffectType.SPEED);
                }
                break;
            case HOPPER:
                permission = "perk.autoloot";
                if (!player.hasPermission(permission)) {
                    player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDu hast keine Berechtigung für §eAuto Loot§c!");
                    return;
                }
                newState = !PerkManager.getPerkStatus(uuid, "autoloot");
                PerkManager.setPerkStatus(uuid, "autoloot", newState);
                perkName = "Auto Loot";
                break;
            case BLAZE_POWDER:
                permission = "perk.strength";
                if (!player.hasPermission(permission)) {
                    player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDu hast keine Berechtigung für §eStrength§c!");
                    return;
                }
                newState = !PerkManager.getPerkStatus(uuid, "strength");
                PerkManager.setPerkStatus(uuid, "strength", newState);
                perkName = "Strength";
                if (newState) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1, false, false));
                } else {
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                }
                break;
            case MAGMA_CREAM:
                permission = "perk.fireresistant";
                if (!player.hasPermission(permission)) {
                    player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDu hast keine Berechtigung für §eFire Resistance§c!");
                    return;
                }
                newState = !PerkManager.getPerkStatus(uuid, "fireresistant");
                PerkManager.setPerkStatus(uuid, "fireresistant", newState);
                perkName = "Fire Resistance";
                if (newState) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                } else {
                    player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                }
                break;
            default:
                return;
        }

        meta.setLore(java.util.Arrays.asList(newState ? "§aAktiviert" : "§cDeaktiviert"));
        clickedItem.setItemMeta(meta);
        player.sendMessage(MCPlugin.getInstance().getPrefix() + (newState ? "§e" + perkName + " §aaktiviert!" : "§e" + perkName + " §cdeaktiviert!"));
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.getAllowFlight() && player.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
            player.setVelocity(player.getLocation().getDirection().multiply(0.8).setY(0.6));
            player.setAllowFlight(false);
            doubleJumpUsed.add(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        boolean doubleJumpActive = PerkManager.getPerkStatus(uuid, "doublejump");

        if (!player.getAllowFlight() && player.isOnGround() && doubleJumpActive) {
            player.setAllowFlight(true);
            doubleJumpUsed.remove(uuid);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL && doubleJumpUsed.contains(player.getUniqueId())) {
                event.setCancelled(true);
                doubleJumpUsed.remove(player.getUniqueId());
            }
        }
    }
}