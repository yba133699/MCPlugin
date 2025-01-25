import ch.plugin.mcplugin.MCPlugin;

import java.sql.DriverManager;
import java.sql.SQLException;

public void connect() {
    try {
        // Verbindung mit zusätzlichem Parameter für die Zeichencodierung
        con = DriverManager.getConnection(
                "jdbc:mysql://" + HOST + ":3306/" + DATABASE + "?useSSL=false&characterEncoding=UTF-8",
                USER,
                PASSWORD
        );
        MCPlugin.sql1.Update("CREATE TABLE IF NOT EXISTS cooldowns (\n" +
                "    player_uuid VARCHAR(36) NOT NULL,\n" +
                "    kit_name VARCHAR(50) NOT NULL,\n" +
                "    last_used BIGINT NOT NULL,\n" +
                "    PRIMARY KEY (player_uuid, kit_name)\n" +
                ");");

        System.out.println("=======================================================");
        System.out.println("|                                                     |");
        System.out.println("|                  MCPLUGIN                           |");
        System.out.println("|      Verbindung zum MySQL Server aufgebaut.         |");
        System.out.println("|                                                     |");
        System.out.println("=======================================================");
    } catch (SQLException e) {
        System.err.println(e);
        System.out.println("=======================================================");
        System.out.println("|                                                     |");
        System.out.println("|                  NemesisCore                        |");
        System.out.println("|     Verbindung zum MySQL Server Fehlgeschlagen!     |");
        System.out.println("|                                                     |");
        System.out.println("=======================================================");
    }
}
