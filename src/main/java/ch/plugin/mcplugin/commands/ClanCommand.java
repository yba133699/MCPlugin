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

import java.util.Map;

import static ch.plugin.mcplugin.manager.ClanManager.getRankPriority;

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
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDer Spieler ist nicht online!");
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

                ClanManager.invitePlayerToClan(playerClanId, target.getUniqueId(), player.getUniqueId());
                target.sendMessage(MCPlugin.instance.getPrefix() + "§7Du wurdest in den Clan eingeladen! Benutze §a/clan accept§7, um beizutreten.");
                player.sendMessage(MCPlugin.instance.getPrefix() + "§aEinladung erfolgreich gesendet!");
                return true;
            case "accept":
                int pendingClanId = ClanManager.getInvitedClanId(player.getUniqueId());
                if (pendingClanId <= 0) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu hast keine ausstehende Clan-Einladung!");
                    return true;
                }

                if (ClanManager.isPlayerInClan(player.getUniqueId())) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu bist bereits in einem Clan!");
                    return true;
                }

                // Spieler dem Clan hinzufügen
                boolean joined = ClanManager.addPlayerToClan(pendingClanId, player.getUniqueId(), "Member");
                if (joined) {
                    // Einladung entfernen
                    ClanManager.removeInvitation(player.getUniqueId());

                    // Erfolgreiche Nachricht
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§aDu bist dem Clan erfolgreich beigetreten!");

                    // Nachricht an den Clan senden
                    String clanName = ClanManager.getClanName(pendingClanId);
                    ClanManager.sendMessageToClan(pendingClanId, "§7[§e" + clanName + "§7] §a" + player.getName() + " §7ist dem Clan beigetreten!");
                } else {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cFehler beim Beitritt zum Clan. Bitte kontaktiere einen Administrator.");
                }
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
                ClanManager.sendMessageToClan(currentClanId, "§7[§eClan§7] §e" + player.getName() + " §7hat den Clan verlassen.");

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

                // Clan-Informationen abrufen
                String clanName = ClanManager.getClanName(infoClanId);
                String clanTag = ClanManager.getClanTag(infoClanId);
                String clanOwner = ClanManager.getClanOwner(infoClanId);
                int memberCount = ClanManager.getClanMemberCount(infoClanId);
                String baseLocation = ClanManager.getBaseLocation(infoClanId);
                String formattedBase = (baseLocation != null && !baseLocation.isEmpty())
                        ? baseLocation.replace(";", ", ")
                        : "§cNicht gesetzt";

                // Mitgliederliste nach Rängen sortieren abrufen
                Map<String, String> membersWithRoles = ClanManager.getMembersWithRoles(infoClanId); // Map<PlayerName, Role>
                StringBuilder sortedMembers = new StringBuilder();

                // Sortiere Mitglieder nach Rang (Owner > Moderator > Member)
                membersWithRoles.entrySet().stream()
                        .sorted((entry1, entry2) -> {
                            String role1 = entry1.getValue();
                            String role2 = entry2.getValue();
                            // Sortiere nach Rangreihenfolge
                            return getRankPriority(role1) - getRankPriority(role2);
                        })
                        .forEach(entry -> {
                            sortedMembers.append("§8 - §e").append(entry.getKey())
                                    .append(" (").append(entry.getValue()).append(")\n");
                        });

                // Schönes Design für die Clan-Info
                player.sendMessage("§8┏╋━━━━━━━━━━━━◥◣§c§lCLAN-INFORMATIONEN§8◢◤━━━━━━━━━━━━╋┓");
                player.sendMessage("");
                player.sendMessage("§8× §7Name: §e" + clanName);
                player.sendMessage("§8× §7Tag: §e[" + clanTag + "]");
                player.sendMessage("§8× §7Besitzer: §e" + clanOwner);
                player.sendMessage("§8× §7Mitglieder: §e" + memberCount);
                player.sendMessage("§8× §7Basis: §e" + formattedBase);
                player.sendMessage("");
                player.sendMessage("§8× §7Mitglieder-Liste:");
                player.sendMessage(sortedMembers.toString().trim());
                player.sendMessage("");
                player.sendMessage("§8┗╋━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━╋┛");
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
                // Überprüfe, ob der Spieler in einem Clan ist
                int baseClanId = ClanManager.getClanIdByPlayer(player.getUniqueId());
                if (baseClanId <= 0) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDu bist in keinem Clan!");
                    return true;
                }

                // Basisstandort abrufen
                String clanBaseLocation = ClanManager.getBaseLocation(baseClanId); // Verwende einen eindeutigen Variablennamen
                if (clanBaseLocation == null || clanBaseLocation.isEmpty()) {
                    player.sendMessage(MCPlugin.instance.getPrefix() + "§cDie Clan-Basis wurde noch nicht gesetzt!");
                    return true;
                }

                // Teile die Koordinaten
                String[] parts = clanBaseLocation.split(";");
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

                    // Spieler zur Basis teleportieren
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
