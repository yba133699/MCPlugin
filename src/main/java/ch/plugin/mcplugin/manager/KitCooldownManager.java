package ch.plugin.mcplugin.manager;

import ch.plugin.mcplugin.MCPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class KitCooldownManager {

    public static Long getCooldown(UUID playerUUID, String kitName) {
        if (playerUUID == null || kitName == null) {
            throw new IllegalArgumentException("playerUUID und kitName dürfen nicht null sein.");
        }

        String query = "SELECT last_used FROM cooldowns WHERE player_uuid = ? AND kit_name = ?";
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUUID.toString());
            stmt.setString(2, kitName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("last_used");
                }
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Abrufen des Cooldowns: " + e.getMessage());
        }
        return 0L; // Kein Cooldown gefunden
    }

    public static void setCooldown(UUID playerUUID, String kitName, long lastUsed) {
        if (playerUUID == null || kitName == null || lastUsed <= 0) {
            throw new IllegalArgumentException("Ungültige Eingabewerte für setCooldown.");
        }

        String query = "INSERT INTO cooldowns (player_uuid, kit_name, last_used) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE last_used = ?";
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUUID.toString());
            stmt.setString(2, kitName);
            stmt.setLong(3, lastUsed);
            stmt.setLong(4, lastUsed);

            stmt.executeUpdate();
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Setzen des Cooldowns: " + e.getMessage());
        }
    }
}
