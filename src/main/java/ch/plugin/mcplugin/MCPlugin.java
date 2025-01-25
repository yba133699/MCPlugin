package ch.plugin.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class MCPlugin extends JavaPlugin {

    public static MCPlugin instance;

    private String noperms;
    private String prefix;

    public static MySQL sql1;

    @Override
    public void onEnable() {

        instance = this;
        // Config laden oder erstellen
        saveDefaultConfig();
        loadConfigValues();

        // MySQL-Verbindung aufbauen
        try {
            sql1 = new MySQL(
                    getConfig().getString("mysql.host"),
                    getConfig().getString("mysql.database"),
                    getConfig().getString("mysql.username"),
                    getConfig().getString("mysql.password")
            );
            sql1.connect();
            getLogger().info("MySQL-Datenbank verbunden.");
        } catch (Exception e) {
            getLogger().severe("MySQL-Verbindung fehlgeschlagen: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Bukkit.getConsoleSender().sendMessage("Plugin started");
        this.getCommand("enderchest").setExecutor(new EnderChestCommand());
        this.getCommand("setspawn").setExecutor(new SpawnCommand());
        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("delspawn").setExecutor(new SpawnCommand());
        this.getCommand("settings").setExecutor(new SettingsCommand());
        this.getCommand("gamemode").setExecutor(new GamemodeCommand());
        this.getCommand("heal").setExecutor(new HealCommand());
        this.getCommand("invsee").setExecutor(new InvseeCommand());
        this.getCommand("kit").setExecutor(new KitCommand());
        this.getCommand("op").setExecutor(new OPCommand());
        this.getCommand("deop").setExecutor(new OPCommand());
        this.getCommand("tp").setExecutor(new TeleportCommand());
        getServer().getPluginManager().registerEvents(new SettingsListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        getServer().getPluginManager().registerEvents(new KitListener(), this);
        //getServer().getPluginManager().registerEvents(new DoubleJumpListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // Singleton-Zugriff
    public static MCPlugin getInstance() {
        return instance;
    }

    // Config-Werte laden
    private void loadConfigValues() {
        this.noperms = getConfig().getString("settings.noperms", "§cDazu hast du keine Rechte!");
        this.prefix = getConfig().getString("settings.prefix", "§8▌ §3NemesisPvP §8• §7");
    }

    public String getNoperms() {
        return noperms;
    }

    public String getPrefix() {
        return prefix;
    }

    public static MySQL getMySQL() {
        return sql1;
    }
}
