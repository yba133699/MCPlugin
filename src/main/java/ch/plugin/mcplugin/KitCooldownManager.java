package ch.plugin.mcplugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class KitCooldownManager {

    public static Long getCooldown(UUID playerUUID, String kitName) {
        String query = "SELECT last_used FROM cooldowns WHERE player_uuid = ? AND kit_name = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUUID.toString());
            stmt.setString(2, kitName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("last_used");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Kein Cooldown gefunden
    }

    public static void setCooldown(UUID playerUUID, String kitName, long lastUsed) {
        String query = "INSERT INTO cooldowns (player_uuid, kit_name, last_used) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE last_used = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUUID.toString());
            stmt.setString(2, kitName);
            stmt.setLong(3, lastUsed);
            stmt.setLong(4, lastUsed);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
