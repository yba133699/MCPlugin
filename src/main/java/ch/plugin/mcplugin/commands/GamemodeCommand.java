package ch.plugin.mcplugin.commands;

import ch.plugin.mcplugin.MCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) {
            s.sendMessage(MCPlugin.getInstance().getPrefix() + "Du kannst den Befehl nicht ausführen");
            return true;
        }
        Player p = (Player) s;
        if (p.hasPermission("mcplugin.gamemode")) {
            if (args.length == 0) {
                p.sendMessage(MCPlugin.getInstance().getPrefix() + "§c/gamemode <mode> <player>");
                return true;
            }
            if (args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "0":
                    case "survival":
                        p.setGameMode(GameMode.SURVIVAL);
                        p.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du bist jetzt im Überlebensmodus");
                        break;
                    case "1":
                    case "creative":
                        p.setGameMode(GameMode.CREATIVE);
                        p.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du bist jetzt im Kreativmodus");
                        break;
                    case "2":
                    case "adventure":
                        p.setGameMode(GameMode.ADVENTURE);
                        p.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du bist jetzt im Abenteuermodus");
                        break;
                    case "3":
                    case "spectator":
                        p.setGameMode(GameMode.SPECTATOR);
                        p.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du bist jetzt im Zuschauermodus");
                        break;
                    default:
                        p.sendMessage(MCPlugin.getInstance().getPrefix() + "§c/gamemode <mode> <player>");
                }
                return true;
            }
        }
        if (p.hasPermission("mcplugin.gamemode.other")) {
            if (args.length == 2) {
                String player = args[1];
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null && target.isOnline()) {
                    switch (args[0].toLowerCase()) {
                        case "0":
                        case "survival":
                            target.setGameMode(GameMode.SURVIVAL);
                            target.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du bist jetzt im Überlebensmodus");
                            p.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §a" + args[1] + " §7ist jetzt im Überlebensmodus");
                            break;
                        case "1":
                        case "creative":
                            target.setGameMode(GameMode.CREATIVE);
                            target.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du bist jetzt im Kreativmodus");
                            p.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §a" + args[1] + " §7ist jetzt im Kreativmodus");
                            break;
                        case "2":
                        case "adventure":
                            target.setGameMode(GameMode.ADVENTURE);
                            target.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du bist jetzt im Abenteuermodus");
                            p.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §a" + args[1] + " §7ist jetzt im Abenteuermodus");
                            break;
                        case "3":
                        case "spectator":
                            target.setGameMode(GameMode.SPECTATOR);
                            target.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du bist jetzt im Zuschauermodus");
                            p.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §a" + args[1] + " §7ist jetzt im Zuschauermodus");
                            break;
                        default:
                            p.sendMessage(MCPlugin.getInstance().getPrefix() + "§c/gamemode <mode>");
                    }
                    return true;
                }
                p.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Der Spieler §c" + args[1] + " §7ist nicht online!");
                return true;
            }
            p.sendMessage(MCPlugin.getInstance().getPrefix() + "§c/gamemode <mode> <player>");
            return true;
        }
        p.sendMessage(MCPlugin.instance.getPrefix() + MCPlugin.instance.getNoperms());
        return true;
    }
}
