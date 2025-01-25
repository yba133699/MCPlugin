package ch.plugin.mcplugin.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private static Connection con;
    private String HOST;
    private String DATABASE;
    private String USER;
    private String PASSWORD;

    public MySQL(String host, String database, String user, String password) {
        this.HOST = host;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;
    }

    public void connect() {
        try {
            // Verbindung mit Parameter für Java 8 kompatibel
            String url = "jdbc:mysql://" + HOST + ":3306/" + DATABASE + "?useSSL=false&characterEncoding=utf8";
            con = DriverManager.getConnection(url, USER, PASSWORD);

            System.out.println("=======================================================");
            System.out.println("|                                                     |");
            System.out.println("|                  MCPLUGIN                           |");
            System.out.println("|      Verbindung zum MySQL Server aufgebaut.         |");
            System.out.println("|                                                     |");
            System.out.println("=======================================================");

            // Tabelle erstellen
            createCooldownTable();
            createClanTable();
            createClanMembersTable();
            createClanInvitationsTable();

        } catch (SQLException e) {
            System.err.println("Fehler bei der MySQL-Verbindung: " + e.getMessage());
            System.out.println("=======================================================");
            System.out.println("|                                                     |");
            System.out.println("|                  NemesisCore                        |");
            System.out.println("|     Verbindung zum MySQL Server Fehlgeschlagen!     |");
            System.out.println("|                                                     |");
            System.out.println("=======================================================");
        }
    }

    public boolean hasConnection() {
        return con != null;
    }

    public void close() {
        try {
            if (con != null) {
                con.close();
                System.out.println("=======================================================");
                System.out.println("|                                                     |");
                System.out.println("|                  NemesisCore                        |");
                System.out.println("|       Verbindung zum MySQL Server getrennt.         |");
                System.out.println("|                                                     |");
                System.out.println("=======================================================");
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Schließen der Verbindung: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            connect(); // Verbindung neu herstellen, falls sie geschlossen ist
        }
        return con;
    }

    private void createCooldownTable() {
        String query = "CREATE TABLE IF NOT EXISTS cooldowns (" +
                "player_uuid VARCHAR(36) NOT NULL, " +
                "kit_name VARCHAR(50) NOT NULL, " +
                "last_used BIGINT NOT NULL, " +
                "PRIMARY KEY (player_uuid, kit_name)" +
                ");";

        try {
            con.createStatement().executeUpdate(query);
            System.out.println("Tabelle 'cooldowns' wurde erfolgreich erstellt (falls nicht vorhanden).");
        } catch (SQLException e) {
            System.err.println("Fehler beim Erstellen der Tabelle: " + e.getMessage());
        }
    }
    private void createClanTable() {
        String query = "CREATE TABLE IF NOT EXISTS clans (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, " +
                "tag VARCHAR(10) NOT NULL, " +
                "owner_uuid VARCHAR(36) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "base_location VARCHAR(255)" +
                ");";

        try {
            con.createStatement().executeUpdate(query);
            System.out.println("Tabelle 'clans' wurde erfolgreich erstellt (falls nicht vorhanden).");
        } catch (SQLException e) {
            System.err.println("Fehler beim Erstellen der Tabelle: " + e.getMessage());
        }
    }
    private void createClanMembersTable() {
        String query = "CREATE TABLE IF NOT EXISTS clan_members (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "clan_id INT NOT NULL, " +
                "player_uuid VARCHAR(36) NOT NULL, " +
                "role VARCHAR(20) DEFAULT 'Member', " +
                "FOREIGN KEY (clan_id) REFERENCES clans(id) ON DELETE CASCADE" +
                ");";

        try {
            con.createStatement().executeUpdate(query);
            System.out.println("Tabelle 'clan_members' wurde erfolgreich erstellt (falls nicht vorhanden).");
        } catch (SQLException e) {
            System.err.println("Fehler beim Erstellen der Tabelle: " + e.getMessage());
        }
    }

    private void createClanInvitationsTable() {
        String query = "CREATE TABLE IF NOT EXISTS clan_invitations (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "clan_id INT NOT NULL, " +
                "player_uuid VARCHAR(36) NOT NULL, " +
                "invited_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (clan_id) REFERENCES clans(id) ON DELETE CASCADE" +
                ");";

        try {
            con.createStatement().executeUpdate(query);
            System.out.println("Tabelle 'clan_invitations' wurde erfolgreich erstellt (falls nicht vorhanden).");
        } catch (SQLException e) {
            System.err.println("Fehler beim Erstellen der Tabelle: " + e.getMessage());
        }
    }

}
