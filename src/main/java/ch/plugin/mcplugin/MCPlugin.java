package ch.plugin.mcplugin;

import ch.plugin.mcplugin.commands.*;
import ch.plugin.mcplugin.events.WorldProtectionListener;
import ch.plugin.mcplugin.listener.*;
import ch.plugin.mcplugin.manager.ChatManager;
import ch.plugin.mcplugin.manager.CombatManager;
import ch.plugin.mcplugin.manager.CrateManager;
import ch.plugin.mcplugin.manager.ScoreboardManager;
import ch.plugin.mcplugin.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class MCPlugin extends JavaPlugin {

    public static MCPlugin instance;

    private String noperms;
    private String prefix;

    public static MySQL sql1;

    @Override
    public void onEnable() {
        instance = this;
        crateManager = new CrateManager();
        combatManager = new CombatManager();
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
            sql1.setupDatabase();
        } catch (Exception e) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        registerCommands();
        registerEvents();

        getLogger().info("MCPlugin wurde erfolgreich gestartet.");
        Bukkit.getScheduler().runTaskTimer(MCPlugin.getInstance(), () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                ScoreboardManager.updatePlayerTeam(p);
            }
        }, 0L, 20L * 10); // Aktualisiert alle 10 Sekunden

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

    private CrateManager crateManager;

    private CombatManager combatManager;


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
        getCommand("tpa").setExecutor(new TeleportCommand());
        getCommand("tpahere").setExecutor(new TeleportCommand());
        getCommand("tpaccept").setExecutor(new TeleportCommand());
        getCommand("clan").setExecutor(new ClanCommand());
        getCommand("msg").setExecutor(new MsgCommand());
        getCommand("r").setExecutor(new MsgCommand());
        getCommand("repair").setExecutor(new RepairCommand());
        getCommand("stack").setExecutor(new StackCommand());
        getCommand("perk").setExecutor(new PerkCommand());

    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new SettingsListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        getServer().getPluginManager().registerEvents(new KitListener(), this);
        getServer().getPluginManager().registerEvents(new ChatManager(), this);
        String protectedWorld = "world"; // Setze hier den Namen der geschützten Welt
        getServer().getPluginManager().registerEvents(new WorldProtectionListener(protectedWorld), this);
        getServer().getPluginManager().registerEvents(new CrateListener(this), this);
        getServer().getPluginManager().registerEvents(new PerkListener(), this);
        getServer().getPluginManager().registerEvents(new CombatListener(combatManager), this);
        getServer().getPluginManager().registerEvents(new MotdListener(), this);
    }


    public CrateManager getCrateManager() {
        return crateManager;
    }
}