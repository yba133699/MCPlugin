package ch.plugin.mcplugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connection;

    public static void connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/mcplugin";
        String username = "NEMESIS";
        String password = "qaywsx190";
        connection = DriverManager.getConnection(url, username, password);
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
