package ch.plugin.mcplugin.commands;

import ch.plugin.mcplugin.MCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class MsgCommand implements CommandExecutor {

    // Speichert die letzte Nachricht für jeden Spieler
    private final HashMap<UUID, UUID> lastMessage = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von einem Spieler verwendet werden.");
            return true;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("msg") || label.equalsIgnoreCase("message")) {
            if (args.length < 2) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cVerwendung: /msg <Spieler> <Nachricht>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null || !target.isOnline()) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§7Der Spieler §c" + args[0] + " §7ist nicht online.");
                return true;
            }

            if (target.equals(player)) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDu kannst dir keine Nachricht selbst senden.");
                return true;
            }

            // Nachricht zusammenfügen
            String message = String.join(" ", args).substring(args[0].length() + 1);

            // Nachrichten senden
            player.sendMessage("§8[§aDu §7-> §e" + target.getName() + "§8] §f" + message);
            target.sendMessage("§8[§e" + player.getName() + " §7-> §aDir§8] §f" + message);

            // Letzten Nachrichtensender speichern
            lastMessage.put(target.getUniqueId(), player.getUniqueId());
            lastMessage.put(player.getUniqueId(), target.getUniqueId());

            return true;
        }

        if (label.equalsIgnoreCase("reply") || label.equalsIgnoreCase("r")) {
            if (args.length < 1) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cVerwendung: /r <Nachricht>");
                return true;
            }

            // Überprüfen, ob ein letzter Nachrichtensender vorhanden ist
            UUID targetUUID = lastMessage.get(player.getUniqueId());
            if (targetUUID == null) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cEs gibt keinen Spieler, an den du antworten kannst.");
                return true;
            }

            Player target = Bukkit.getPlayer(targetUUID);
            if (target == null || !target.isOnline()) {
                player.sendMessage(MCPlugin.getInstance().getPrefix() + "§cDer Spieler, an den du antworten möchtest, ist nicht mehr online.");
                return true;
            }

            // Nachricht zusammenfügen
            String message = String.join(" ", args);

            // Nachrichten senden
            player.sendMessage("§8[§aDu §7-> §e" + target.getName() + "§8] §f" + message);
            target.sendMessage("§8[§e" + player.getName() + " §7-> §aDir§8] §f" + message);

            // Letzten Nachrichtensender aktualisieren
            lastMessage.put(target.getUniqueId(), player.getUniqueId());
            lastMessage.put(player.getUniqueId(), target.getUniqueId());

            return true;
        }

        return false;
    }
}
