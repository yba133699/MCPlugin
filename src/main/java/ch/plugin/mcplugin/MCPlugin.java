package ch.plugin.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class MCPlugin extends JavaPlugin {

    public static MCPlugin instance;

    private final String noperms = "§cDazu hast du keine Rechte!";

    private final String prefix = "§6§lServer §8│ §7";

    @Override
    public void onEnable() {
        instance = this;
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

    public static MCPlugin getInstance() {
        return instance;

    }

    public final String noperms() {
        return noperms;

    }

    public final String prefix() {
        return prefix;

    }
}
