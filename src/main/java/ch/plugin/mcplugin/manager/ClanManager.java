package ch.plugin.mcplugin.manager;

import ch.plugin.mcplugin.MCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ClanManager {

    // Erstellt einen neuen Clan und gibt die Clan-ID zurück
    public static int createClan(String name, String tag, UUID ownerUUID) {
        String query = "INSERT INTO clans (name, tag, owner_uuid, created_at) VALUES (?, ?, ?, NOW())";

        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, tag);
            stmt.setString(3, ownerUUID.toString());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Clan-ID zurückgeben
                }
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Erstellen des Clans: " + e.getMessage());
        }
        return -1; // Fehler
    }

    // Überprüft, ob ein Spieler in einem Clan ist
    public static boolean isPlayerInClan(UUID playerUUID) {
        String query = "SELECT * FROM clan_members WHERE player_uuid = ?";

        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUUID.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Spieler gefunden
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Überprüfen der Clan-Mitgliedschaft: " + e.getMessage());
        }
        return false;
    }

    // Fügt einen Spieler einem Clan hinzu
    public static void addPlayerToClan(int clanId, UUID playerUUID, String role) {
        String query = "INSERT INTO clan_members (clan_id, player_uuid, role) VALUES (?, ?, ?)";

        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clanId);
            stmt.setString(2, playerUUID.toString());
            stmt.setString(3, role);
            stmt.executeUpdate();
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Hinzufügen eines Spielers zum Clan: " + e.getMessage());
        }
    }

    // Setzt die Basis eines Clans
    public static void setBaseLocation(int clanId, String location) {
        String query = "UPDATE clans SET base_location = ? WHERE id = ?";

        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, location);
            stmt.setInt(2, clanId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Setzen der Clanbasis: " + e.getMessage());
        }
    }

    // Holt die Basis eines Clans
    public static String getBaseLocation(int clanId) {
        String query = "SELECT base_location FROM clans WHERE id = ?";

        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clanId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("base_location");
                }
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Abrufen der Clanbasis: " + e.getMessage());
        }
        return null;
    }

    // Holt die Clan-ID eines Spielers
    public static int getClanIdByPlayer(UUID playerUUID) {
        String query = "SELECT clan_id FROM clan_members WHERE player_uuid = ?";
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUUID.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("clan_id"); // Clan-ID zurückgeben
                }
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Abrufen der Clan-ID: " + e.getMessage());
        }
        return -1; // Kein Clan gefunden
    }


    // Überprüft, ob ein Spieler der Besitzer eines Clans ist
    public static boolean isPlayerClanOwner(UUID playerUUID) {
        String query = "SELECT * FROM clans WHERE owner_uuid = ?";
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUUID.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Spieler ist Besitzer
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Überprüfen des Clanbesitzers: " + e.getMessage());
        }
        return false;
    }

    // Zählt die Mitglieder eines Clans
    public static int getClanMemberCount(int clanId) {
        String query = "SELECT COUNT(*) AS count FROM clan_members WHERE clan_id = ?";
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clanId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count"); // Anzahl der Mitglieder
                }
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Zählen der Clanmitglieder: " + e.getMessage());
        }
        return 0;
    }

    // Überprüft, ob ein Clan-Name oder Tag vergeben ist
    public static boolean isClanNameOrTagTaken(String name, String tag) {
        String query = "SELECT * FROM clans WHERE name = ? OR tag = ?";
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, tag);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Gibt true zurück, wenn Name oder Tag existiert
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Überprüfen von Name oder Tag: " + e.getMessage());
        }
        return false; // Keine Treffer gefunden
    }

    // Überprüft, ob ein Spieler Besitzer oder Moderator ist
    public static boolean isPlayerClanOwnerOrModerator(UUID playerUUID) {
        String query = "SELECT role FROM clan_members WHERE player_uuid = ?";
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUUID.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    return role.equalsIgnoreCase("Owner") || role.equalsIgnoreCase("Moderator");
                }
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Überprüfen der Clanrolle: " + e.getMessage());
        }
        return false;
    }

    // Einladung eines Spielers in den Clan
    public static void invitePlayerToClan(int clanId, UUID playerUUID) {
        String query = "INSERT INTO clan_invitations (clan_id, player_uuid, invited_at) VALUES (?, ?, NOW())";
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clanId);
            stmt.setString(2, playerUUID.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Einladen des Spielers in den Clan: " + e.getMessage());
        }
    }

    // Einladung eines Spielers abrufen
    public static int getInvitedClanId(UUID playerUUID) {
        String query = "SELECT clan_id FROM clan_invitations WHERE player_uuid = ?";
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUUID.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("clan_id");
                }
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Abrufen der Einladung: " + e.getMessage());
        }
        return -1; // Keine Einladung gefunden
    }

    // Einladung entfernen
    public static void removeInvitation(UUID playerUUID) {
        String query = "DELETE FROM clan_invitations WHERE player_uuid = ?";
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUUID.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Entfernen der Einladung: " + e.getMessage());
        }
    }

    // Clan-ID anhand des Namens abrufen
    public static int getClanIdByName(String name) {
        String query = "SELECT id FROM clans WHERE name = ?";
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Abrufen der Clan-ID: " + e.getMessage());
        }
        return -1; // Clan nicht gefunden
    }

    // Spieler aus einem Clan entfernen
    public static void removePlayerFromClan(int clanId, UUID playerUUID) {
        String query = "DELETE FROM clan_members WHERE clan_id = ? AND player_uuid = ?";
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clanId);
            stmt.setString(2, playerUUID.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Entfernen des Spielers aus dem Clan: " + e.getMessage());
        }
    }

    // Clan löschen
    public static void deleteClan(int clanId) {
        String queryClan = "DELETE FROM clans WHERE id = ?";
        String queryMembers = "DELETE FROM clan_members WHERE clan_id = ?";
        String queryInvitations = "DELETE FROM clan_invitations WHERE clan_id = ?";
        try (Connection conn = MCPlugin.getMySQL().getConnection()) {
            // Clan löschen
            try (PreparedStatement stmt = conn.prepareStatement(queryClan)) {
                stmt.setInt(1, clanId);
                stmt.executeUpdate();
            }
            // Mitglieder löschen
            try (PreparedStatement stmt = conn.prepareStatement(queryMembers)) {
                stmt.setInt(1, clanId);
                stmt.executeUpdate();
            }
            // Einladungen löschen
            try (PreparedStatement stmt = conn.prepareStatement(queryInvitations)) {
                stmt.setInt(1, clanId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Löschen des Clans: " + e.getMessage());
        }
    }
    public static String getClanInfo(int clanId) {
        StringBuilder info = new StringBuilder();
        String queryClan = "SELECT * FROM clans WHERE id = ?";
        String queryMembers = "SELECT player_uuid, role FROM clan_members WHERE clan_id = ?";

        try (Connection conn = MCPlugin.getMySQL().getConnection()) {
            // Clan-Informationen abrufen
            try (PreparedStatement stmt = conn.prepareStatement(queryClan)) {
                stmt.setInt(1, clanId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        info.append("§6Clan-Name: §f").append(rs.getString("name")).append("\n");
                        info.append("§6Tag: §f").append(rs.getString("tag")).append("\n");
                        info.append("§6Besitzer: §f").append(Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("owner_uuid"))).getName()).append("\n");
                        info.append("§6Erstellt am: §f").append(rs.getTimestamp("created_at")).append("\n");
                    }
                }
            }

            // Mitgliederliste abrufen
            info.append("§6Mitglieder:\n");
            try (PreparedStatement stmt = conn.prepareStatement(queryMembers)) {
                stmt.setInt(1, clanId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String playerName = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("player_uuid"))).getName();
                        String role = rs.getString("role");
                        info.append("§7 - ").append(playerName).append(" (").append(role).append(")\n");
                    }
                }
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Abrufen der Clan-Informationen: " + e.getMessage());
        }

        return info.toString();
    }


    // Nachricht an Clanmitglieder senden
    public static void sendMessageToClan(int clanId, String message) {
        String query = "SELECT player_uuid FROM clan_members WHERE clan_id = ?";
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clanId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UUID playerUUID = UUID.fromString(rs.getString("player_uuid"));
                    Player player = Bukkit.getPlayer(playerUUID);
                    if (player != null && player.isOnline()) {
                        player.sendMessage(message);
                    }
                }
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Senden der Nachricht an den Clan: " + e.getMessage());
        }
    }
    public static String getClanTag(int clanId) {
        String query = "SELECT tag FROM clans WHERE id = ?";
        try (Connection conn = MCPlugin.getMySQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clanId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tag");
                }
            }
        } catch (SQLException e) {
            MCPlugin.getInstance().getLogger().severe("Fehler beim Abrufen des Clan-Tags: " + e.getMessage());
        }
        return null;
    }
}
