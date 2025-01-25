package ch.plugin.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage("Du kannst diesen Befehl nicht ausführen!");
            return true;
        }
        Player p = (Player) s;
        switch (label.toLowerCase()) {
            case "setspawn":
                if (p.hasPermission("mcplugin.setspawn")) {
                    setSpawn(p);
                    p.sendMessage(MCPlugin.getInstance().getPrefix() + "Der Spawn wurde gesetzt");
                    return true;
                }
                p.sendMessage(MCPlugin.getInstance().getPrefix() + MCPlugin.getInstance().getNoperms());
                return true;

            case "spawn":
                teleportToSpawn(p);
                return true;

            case "delspawn":
                if (p.hasPermission("mcplugin.delspawn")) {
                    deleteSpawn();
                    p.sendMessage(MCPlugin.getInstance().getPrefix() + "Du hast den Spawn gelöscht");
                    return true;
                }
                p.sendMessage(MCPlugin.getInstance().getPrefix() + MCPlugin.getInstance().getNoperms());
                return true;
        }
        return false;
    }

    private void setSpawn(Player player) {
        Location spawnLocation = player.getLocation();
        FileConfiguration config = MCPlugin.getInstance().getConfig();
        config.set("spawn.world", spawnLocation.getWorld().getName());
        config.set("spawn.x", spawnLocation.getX());
        config.set("spawn.y", spawnLocation.getY());
        config.set("spawn.z", spawnLocation.getZ());
        config.set("spawn.pitch", spawnLocation.getPitch());
        config.set("spawn.yaw", spawnLocation.getYaw());
        MCPlugin.getInstance().saveConfig();
    }

    // Teleportiert den Spieler zum gespeicherten Spawnpunkt
    private void teleportToSpawn(Player player) {
        FileConfiguration config = MCPlugin.getInstance().getConfig();
        if (config.contains("spawn")) {
            String worldName = config.getString("spawn.world");
            double x = config.getDouble("spawn.x");
            double y = config.getDouble("spawn.y");
            double z = config.getDouble("spawn.z");
            float pitch = (float) config.getDouble("spawn.pitch");
            float yaw = (float) config.getDouble("spawn.yaw");

            Location spawnLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            player.teleport(spawnLocation);
            player.sendMessage(MCPlugin.getInstance().getPrefix() + "§aDu wurdest zum Spawnpunkt teleportiert!");
        } else {
            player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cEs wurde noch kein Spawnpunkt gesetzt.");
        }
    }

    // Löscht den gespeicherten Spawnpunkt
    private void deleteSpawn() {
        FileConfiguration config = MCPlugin.getInstance().getConfig();
        config.set("spawn", null);
        MCPlugin.getInstance().saveConfig();
    }
}

