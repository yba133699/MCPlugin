package ch.plugin.mcplugin.commands;

import ch.plugin.mcplugin.MCPlugin;
import ch.plugin.mcplugin.manager.ChatManager;
import ch.plugin.mcplugin.manager.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler verwendet werden.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // Help-Nachrichten für das Clan-System
            player.sendMessage("§8┏╋━━━━━━━━━━━━◥◣§c§lCLAN-SYSTEM§8◢◤━━━━━━━━━━━━╋┓");
            player.sendMessage("");
            player.sendMessage("§8× §7/clan create <name> <tag> §8| §eClan erstellen");
            player.sendMessage("§8× §7/clan invite <player> §8| §eSpieler in Clan einladen");
            player.sendMessage("§8× §7/clan accept §8| §eClan beitreten");
            player.sendMessage("§8× §7/clan leave §8| §eClan verlassen");
            player.sendMessage("§8× §7/clan delete §8| §eClan löschen");
            player.sendMessage("§8× §7/clan info §8| §eInformationen über den Clan");
            player.sendMessage("§8× §7/clan tag <tag> §8| §eClan-Tag ändern");
            player.sendMessage("§8× §7/clan setbase §8| §eClan-Basis setzen");
            player.sendMessage("§8× §7/clan base §8| §eZu Clan-Basis teleportieren");
            player.sendMessage("§8× §7/clan chat §8| §eIm Clan chatten");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                if (args.length < 3) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cVerwendung: /clan create <name> <tag>");
                    return true;
                }
                String name = args[1];
                String tag = args[2];

                if (ClanManager.isPlayerInClan(player.getUniqueId())) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu bist bereits in einem Clan!");
                    return true;
                }

                if (ClanManager.isClanNameOrTagTaken(name, tag)) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cName oder Tag ist bereits vergeben!");
                    return true;
                }

                int newClanId = ClanManager.createClan(name, tag, player.getUniqueId());
                if (newClanId > 0) {
                    ClanManager.addPlayerToClan(newClanId, player.getUniqueId(), "Owner");
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§aClan erfolgreich erstellt!");
                } else {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cFehler beim Erstellen des Clans.");
                }
                return true;

            case "invite":
                if (args.length < 2) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cVerwendung: /clan invite <player>");
                    return true;
                }

                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);

                if (target == null) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "MCPlugin.instance.getPrefix() + §cDer Spieler ist nicht online!");
                    return true;
                }

                int playerClanId = ClanManager.getClanIdByPlayer(player.getUniqueId());
                if (playerClanId <= 0) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu bist in keinem Clan!");
                    return true;
                }

                if (!ClanManager.isPlayerClanOwnerOrModerator(player.getUniqueId())) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu hast keine Berechtigung, Spieler einzuladen!");
                    return true;
                }

                if (ClanManager.isPlayerInClan(target.getUniqueId())) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDieser Spieler ist bereits in einem Clan!");
                    return true;
                }

                ClanManager.invitePlayerToClan(playerClanId, target.getUniqueId());
                target.sendMessage(MCPlugin.instance.getPrefix() + "§aDu wurdest in den Clan eingeladen! Benutze /clan accept, um beizutreten.");
                player.sendMessage(MCPlugin.instance.getPrefix() + "§aEinladung erfolgreich gesendet!");
                return true;
            case "accept":
                int invitedClanId = ClanManager.getInvitedClanId(player.getUniqueId());
                if (invitedClanId <= 0) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu hast keine offenen Einladungen!");
                    return true;
                }

                ClanManager.addPlayerToClan(invitedClanId, player.getUniqueId(), "Member");
                ClanManager.removeInvitation(player.getUniqueId());
                player.sendMessage(MCPlugin.instance.getPrefix() + "§aDu bist dem Clan erfolgreich beigetreten!");
                return true;
            case "leave":
                int currentClanId = ClanManager.getClanIdByPlayer(player.getUniqueId());
                if (currentClanId <= 0) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu bist in keinem Clan!");
                    return true;
                }

                // Überprüfen, ob der Spieler der Besitzer ist und der letzte im Clan
                if (ClanManager.isPlayerClanOwner(player.getUniqueId())) {
                    int memberCount = ClanManager.getClanMemberCount(currentClanId);
                    if (memberCount <= 1) {
                        player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu bist der letzte Spieler im Clan und kannst ihn nicht verlassen. Lösche den Clan stattdessen mit /clan delete.");
                        return true;
                    }
                }

                // Spieler aus dem Clan entfernen
                ClanManager.removePlayerFromClan(currentClanId, player.getUniqueId());
                player.sendMessage(MCPlugin.instance.getPrefix() + "§aDu hast den Clan erfolgreich verlassen!");
                return true;

            case "delete":
                int deleteClanId = ClanManager.getClanIdByPlayer(player.getUniqueId());
                if (deleteClanId <= 0) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu bist in keinem Clan!");
                    return true;
                }

                if (!ClanManager.isPlayerClanOwner(player.getUniqueId())) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cNur der Clanbesitzer kann den Clan löschen!");
                    return true;
                }

                ClanManager.deleteClan(deleteClanId);
                player.sendMessage(MCPlugin.instance.getPrefix() + "§aDer Clan wurde erfolgreich gelöscht!");
                return true;
            case "chat":
                int chatClanId = ClanManager.getClanIdByPlayer(player.getUniqueId());
                if (args.length < 2) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cVerwendung: /clan chat <message>");
                    return true;
                }
                if (chatClanId <= 0) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu bist in keinem Clan!");
                    return true;
                }
                String message = String.join(" ", args).substring(args[0].length() + 1);

                ClanManager.sendMessageToClan(chatClanId, "§7[§eClan§7] §e" + player.getName() + "§8» §7 " + message);
                return true;

            case "info":
                int infoClanId = ClanManager.getClanIdByPlayer(player.getUniqueId());
                if (infoClanId <= 0) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu bist in keinem Clan!");
                    return true;
                }

                String clanInfo = ClanManager.getClanInfo(infoClanId);
                player.sendMessage(clanInfo);
                return true;


            case "setbase":
                int clanId = ClanManager.getClanIdByPlayer(player.getUniqueId());
                if (clanId > 0) {
                    String location = player.getLocation().getWorld().getName() + ";" +
                            player.getLocation().getBlockX() + ";" +
                            player.getLocation().getBlockY() + ";" +
                            player.getLocation().getBlockZ();
                    ClanManager.setBaseLocation(clanId, location);
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§aClanbasis erfolgreich gesetzt!");
                } else {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu bist in keinem Clan!");
                }
                break;
            case "base":
                int baseClanId = ClanManager.getClanIdByPlayer(player.getUniqueId()); // Verwende eine eindeutige Variablenbezeichnung
                if (baseClanId <= 0) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu bist in keinem Clan!");
                    return true;
                }

                String baseLocation = ClanManager.getBaseLocation(baseClanId);
                if (baseLocation == null || baseLocation.isEmpty()) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDie Clan-Basis wurde noch nicht gesetzt!");
                    return true;
                }

                // Format: world;x;y;z
                String[] parts = baseLocation.split(";");
                if (parts.length != 4) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDie Clan-Basis ist ungültig. Bitte kontaktiere einen Admin.");
                    return true;
                }

                try {
                    World world = Bukkit.getWorld(parts[0]);
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double z = Double.parseDouble(parts[3]);

                    if (world == null) {
                        player.sendMessage(MCPlugin.instance.getPrefix() + "§cDie Clan-Basis existiert nicht mehr!");
                        return true;
                    }

                    Location location = new Location(world, x, y, z);
                    player.teleport(location);
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§aDu wurdest zur Clan-Basis teleportiert!");
                } catch (NumberFormatException e) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDie Clan-Basis hat ungültige Koordinaten. Bitte kontaktiere einen Admin.");
                }
                return true;


            default:
                // Help-Nachrichten für das Clan-System
                player.sendMessage("§8┏╋━━━━━━━━━━━━◥◣§c§lCLAN-SYSTEM§8◢◤━━━━━━━━━━━━╋┓");
                player.sendMessage("");
                player.sendMessage("§8× §7/clan create <name> <tag> §8| §eClan erstellen");
                player.sendMessage("§8× §7/clan invite <player> §8| §eSpieler in Clan einladen");
                player.sendMessage("§8× §7/clan accept §8| §eClan beitreten");
                player.sendMessage("§8× §7/clan leave §8| §eClan verlassen");
                player.sendMessage("§8× §7/clan delete §8| §eClan löschen");
                player.sendMessage("§8× §7/clan info §8| §eInformationen über den Clan");
                player.sendMessage("§8× §7/clan setbase §8| §eClan-Basis setzen");
                player.sendMessage("§8× §7/clan base §8| §eZu Clan-Basis teleportieren");
                player.sendMessage("§8× §7/clan chat §8| §eIm Clan chatten");
        }

        return true;
    }
}
