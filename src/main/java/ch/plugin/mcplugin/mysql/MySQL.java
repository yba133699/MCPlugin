package ch.plugin.mcplugin.mysql;

import java.sql.*;

public class MySQL {

    private static Connection con;
    private final String HOST;
    private final String DATABASE;
    private final String USER;
    private final String PASSWORD;

    public MySQL(String host, String database, String user, String password) {
        this.HOST = host;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;
    }

    public void connect() {
        try {
            if (con == null || con.isClosed()) {
                String url = "jdbc:mysql://" + HOST + ":3306/" + DATABASE + "?useSSL=false&characterEncoding=utf8";
                con = DriverManager.getConnection(url, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("[MCPlugin] Fehler beim Aufbau der Verbindung: " + e.getMessage());
            con = null;
        }
    }

    public boolean hasConnection() {
        try {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            System.err.println("[MCPlugin] Fehler beim Überprüfen der Verbindung: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                con = null;
            }
        } catch (SQLException e) {
            System.err.println("[MCPlugin] Fehler beim Schließen der Verbindung: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.err.println("[MCPlugin] Fehler beim Überprüfen der MySQL-Verbindung: " + e.getMessage());
        }
        return con;
    }

    public void setupDatabase() {
        if (!hasConnection()) {
            System.err.println("[MCPlugin] Keine Verbindung zur Datenbank. Setup wird übersprungen.");
            return;
        }
        createPerkTable();
    }

    private void createPerkTable() {
        executeUpdate("CREATE TABLE IF NOT EXISTS perks (" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "doublejump BOOLEAN NOT NULL DEFAULT FALSE, " +
                "autoloot BOOLEAN NOT NULL DEFAULT FALSE, " +
                "speed BOOLEAN NOT NULL DEFAULT FALSE, " +
                "strength BOOLEAN NOT NULL DEFAULT FALSE, " +
                "fireresistant BOOLEAN NOT NULL DEFAULT FALSE);", "perks");
    }


    private void executeUpdate(String query, String tableName) {
        try {
            con.createStatement().executeUpdate(query);
            System.out.println("[MCPlugin] Tabelle '" + tableName + "' wurde erfolgreich erstellt (falls nicht vorhanden).");
        } catch (SQLException e) {
            System.err.println("[MCPlugin] Fehler beim Erstellen der Tabelle '" + tableName + "': " + e.getMessage());
        }
    }
}