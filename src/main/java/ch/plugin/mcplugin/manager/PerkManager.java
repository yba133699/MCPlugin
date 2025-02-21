package ch.plugin.mcplugin.manager;

import ch.plugin.mcplugin.MCPlugin;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class PerkManager {

    public static boolean getPerkStatus(UUID uuid, String perk) {
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT " + perk + " FROM perks WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(perk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setPerkStatus(UUID uuid, String perk, boolean status) {
        String query = "INSERT INTO perks (uuid, " + perk + ") VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE " + perk + " = VALUES(" + perk + ")";

        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, uuid.toString());
            ps.setBoolean(2, status);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void loadPerks(Player player) {
        UUID uuid = player.getUniqueId();
        boolean doubleJumpActive = getPerkStatus(uuid, "doublejump");
        boolean speedActive = getPerkStatus(uuid, "speed");
        boolean autoLootActive = getPerkStatus(uuid, "autoloot");
        boolean strengthActive = getPerkStatus(uuid, "strength");
        boolean fireResistantActive = getPerkStatus(uuid, "fireresistant");

        // Double Jump setzen
        player.setAllowFlight(doubleJumpActive);

        // Speed Perk setzen
        if (speedActive) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.SPEED);
        }

        // Strength Perk setzen
        if (strengthActive) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }

        // Fire Resistance Perk setzen
        if (fireResistantActive) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        }

        // Auto Loot setzen (Falls du hier eine konkrete Implementierung benötigst, ergänze sie in den Events)
        if (autoLootActive) {
            player.sendMessage("§aAuto Loot ist aktiviert!");
        }
    }
}
