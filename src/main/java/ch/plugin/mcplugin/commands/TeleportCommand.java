package ch.plugin.mcplugin.commands;

import ch.plugin.mcplugin.MCPlugin;
import ch.plugin.mcplugin.manager.TpaRequestManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TeleportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

        final HashMap<UUID, Long> cooldowns = new HashMap<>();

        if (!(s instanceof Player)) {
            if(label.equalsIgnoreCase("tp")) {
                if (args.length == 2) {
                    Player p1 = Bukkit.getPlayer(args[0]);
                    Player p2 = Bukkit.getPlayer(args[1]);
                    if (p1 != null && p2 != null) {
                        p1.teleport(p2.getLocation());
                        s.sendMessage(MCPlugin.instance.getPrefix() + "§7Du hast §a" + p1.getName() + " §7zu §a" + p2.getName() + " §7teleportiert");
                        p1.sendMessage(MCPlugin.instance.getPrefix() + "§7Du wurdest zu §a" + p2.getName() + " §7teleportiert");
                        p2.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §a" + p1.getName() + " §7wurde zu dir teleportiert");
                        return true;
                    }
                    if (p1 == null && p2 != null) {
                        s.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online");
                        return true;
                    }
                    if (p1 != null && p2 == null) {
                        s.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §c" + args[1] + " §7ist nicht online");
                        return true;
                    }
                    s.sendMessage(MCPlugin.instance.getPrefix() + "§cBeide Spieler sind nicht online");
                    return true;
                }
                s.sendMessage(MCPlugin.instance.getPrefix() + "§cBenute /tp <player> <target>");
                return true;
            }
            s.sendMessage(MCPlugin.instance.getPrefix() + "§cBenute /tp <player> <target>");
            return true;
        }

        Player p = (Player) s;
        if(label.equalsIgnoreCase("tp")) {
            if (p.hasPermission("mcplugin.tp") && !(p.hasPermission("mcplugin.tp.other"))) {
                if (args.length == 1) {

                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        p.teleport(target.getLocation());
                        p.sendMessage(MCPlugin.instance.getPrefix() + "§7Du wurdest zu §a" + target.getName() + " §7teleportiert");
                        return true;
                    }
                    p.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online");
                    return true;
                }
                if(args.length == 2) {
                    p.sendMessage(MCPlugin.instance.getPrefix() + MCPlugin.instance.getNoperms());
                    return true;
                }
                s.sendMessage(MCPlugin.instance.getPrefix() + "§cBenute /tp <player> <target>");
                return true;
            }
            if(p.hasPermission("mcplugin.tp.other") ) {
                if(args.length == 2) {

                    Player p1 = Bukkit.getPlayer(args[0]);
                    Player p2 = Bukkit.getPlayer(args[1]);
                    if (p1 != null && p2 != null) {
                        p1.teleport(p2.getLocation());
                        s.sendMessage(MCPlugin.instance.getPrefix() + "§7Du hast §a" + p1.getName() + " §7zu §a" + p2.getName() + " §7teleportiert");
                        p1.sendMessage(MCPlugin.instance.getPrefix() + "§7Du wurdest zu §a" + p2.getName() + " §7teleportiert");
                        p2.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §a" + p1.getName() + " §7wurde zu dir teleportiert");
                        return true;
                    }
                    if (p1 == null && p2 != null) {
                        s.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online");
                        return true;
                    }
                    if (p1 != null && p2 == null) {
                        s.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §c" + args[1] + " §7ist nicht online");
                        return true;
                    }
                    s.sendMessage(MCPlugin.instance.getPrefix() + "§cBeide Spieler sind nicht online");
                    return true;
                }
                if(args.length == 1) {
                    if (p.hasPermission("mcplugin.tp")) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            p.teleport(target.getLocation());
                            p.sendMessage(MCPlugin.instance.getPrefix() + "§7Du wurdest zu §a" + target.getName() + " §7teleportiert");
                            return true;
                        }
                        p.sendMessage(MCPlugin.instance.getPrefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online");
                        return true;
                    }
                    p.sendMessage(MCPlugin.instance.getPrefix() + MCPlugin.instance.getNoperms());
                    return true;
                }
                p.sendMessage(MCPlugin.instance.getPrefix() + "§cBenute /tp <player> <target>");
                return true;
            }
            p.sendMessage(MCPlugin.instance.getPrefix() + MCPlugin.instance.getNoperms());
            return true;
        }
        Player player = (Player) s;

        if (label.equalsIgnoreCase("tpa")) {
            if (args.length != 1) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cVerwendung: /tpa <Spieler>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDer Spieler §7" + args[0] + " §cist nicht online.");
                return true;
            }

            //if (target.equals(player)) {
            //    player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDu kannst keine Teleportanfrage an dich selbst senden.");
            //    return true;
            //}

            // Anfrage senden
            TpaRequestManager.addRequest(player.getUniqueId(), target.getUniqueId(), false); // "false" für TPA
            target.sendMessage(MCPlugin.getInstance().getPrefix() + "§a" + player.getName() + " §7möchte sich zu dir teleportieren.");
            target.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Benutze §e/tpaccept §7, um die Anfrage anzunehmen.");
            player.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Teleport-Anfrage an §a" + target.getName() + " §7gesendet.");
            return true;
        }

        if (label.equalsIgnoreCase("tpahere")) {
            if (args.length != 1) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cVerwendung: /tpahere <player>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online.");
                return true;
            }

            if (target.equals(player)) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDu kannst keine Teleportanfrage an dich selbst senden.");
                return true;
            }

            // Anfrage senden
            TpaRequestManager.addRequest(player.getUniqueId(), target.getUniqueId(), true); // "true" für TPAHere
            target.sendMessage(MCPlugin.getInstance().getPrefix() + "§a" + player.getName() + " §7möchte, dass du dich zu ihm teleportierst.");
            target.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Benutze §e/tpaccept §7, um die Anfrage anzunehmen.");
            player.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Teleport-Anfrage an §a" + target.getName() + " §7gesendet.");
            return true;
        }

        if (label.equalsIgnoreCase("tpaccept")) {
            Player requester = TpaRequestManager.getRequester(player.getUniqueId());
            if (requester == null) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDu hast keine Teleportanfragen.");
                return true;
            }

            boolean isTpaHere = TpaRequestManager.isTpaHere(player.getUniqueId());

            if (isTpaHere) {
                // Anfrage für "tpahere" akzeptieren
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§a" + requester.getName() + " §7wird in 3 Sekunden zu dir teleportiert.");
                requester.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du wirst in 3 Sekunden zu §a" + player.getName() + " §7teleportiert.");

                // Countdown starten
                Bukkit.getScheduler().runTaskLater(MCPlugin.getInstance(), () -> {
                    requester.teleport(player.getLocation());
                    player.sendMessage(MCPlugin.getInstance().getPrefix() + "§a" + requester.getName() + " §7wurde zu dir teleportiert.");
                    requester.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du wurdest zu §a" + player.getName() + " §7teleportiert.");
                }, 60L); // 60 Ticks = 3 Sekunden
            } else {
                // Anfrage für "tpa" akzeptieren
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du wirst in 3 Sekunden zu §a" + requester.getName() + " §7teleportiert.");
                requester.sendMessage(MCPlugin.getInstance().getPrefix() + "§a" + player.getName() + " §7wird in 3 Sekunden zu dir teleportiert.");

                // Countdown starten
                Bukkit.getScheduler().runTaskLater(MCPlugin.getInstance(), () -> {
                    player.teleport(requester.getLocation());
                    player.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Du wurdest zu §a" + requester.getName() + " §7teleportiert.");
                    requester.sendMessage(MCPlugin.getInstance().getPrefix() + "§a" + player.getName() + " §7wurde zu dir teleportiert.");
                }, 60L); // 60 Ticks = 3 Sekunden
            }

            TpaRequestManager.removeRequest(player.getUniqueId());
            return true;
        }


        return false;
    }
}
