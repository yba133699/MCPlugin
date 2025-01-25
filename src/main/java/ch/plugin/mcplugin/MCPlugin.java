package ch.plugin.mcplugin;

import ch.plugin.mcplugin.commands.*;
import ch.plugin.mcplugin.manager.ChatManager;
import ch.plugin.mcplugin.mysql.MySQL;
import ch.plugin.mcplugin.listener.JoinListener;
import ch.plugin.mcplugin.listener.KitListener;
import ch.plugin.mcplugin.listener.RespawnListener;
import ch.plugin.mcplugin.listener.SettingsListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCPlugin extends JavaPlugin {

    public static MCPlugin instance;

    private String noperms;
    private String prefix;

    public static MySQL sql1;

    @Override
    public void onEnable() {
        if (instance != null) {
            throw new IllegalStateException("MCPlugin wurde bereits initialisiert!");
        }
        instance = this;

        saveDefaultConfig();
        reloadConfig();
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
        } catch (Exception e) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        registerCommands();
        registerEvents();

        getLogger().info("MCPlugin wurde erfolgreich gestartet.");
    }

    @Override
    public void onDisable() {
        if (sql1 != null && sql1.hasConnection()) {
            sql1.close();
        }
        getLogger().info("MCPlugin wurde deaktiviert.");
    }

    public static MCPlugin getInstance() {
        return instance;
    }

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

    private void registerCommands() {
        getCommand("enderchest").setExecutor(new EnderChestCommand());
        getCommand("setspawn").setExecutor(new SpawnCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("delspawn").setExecutor(new SpawnCommand());
        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("gamemode").setExecutor(new GamemodeCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("invsee").setExecutor(new InvseeCommand());
        getCommand("kit").setExecutor(new KitCommand());
        getCommand("op").setExecutor(new OPCommand());
        getCommand("deop").setExecutor(new OPCommand());
        getCommand("tp").setExecutor(new TeleportCommand());
        getCommand("clan").setExecutor(new ClanCommand());
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new SettingsListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        getServer().getPluginManager().registerEvents(new KitListener(), this);
        getServer().getPluginManager().registerEvents(new ChatManager(), this);
    }
}