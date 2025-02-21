package ch.plugin.mcplugin.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ScoreboardManager {

    public static void setScoreboard(Player p, String clanName) {
        // Create a new scoreboard
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        // Sidebar Objective
        Objective obj = board.registerNewObjective("aaa", "dummy");
        obj.setDisplayName("§3NemesisPvP.de");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.getScore("9").setScore(9);
        obj.getScore("§cStatistiken").setScore(8);
        obj.getScore("§7Kills §8» §b0").setScore(7);
        obj.getScore("§7Tode §8» §b0").setScore(6);
        obj.getScore("§7Killstreak §8» §b0").setScore(5);
        obj.getScore("§7K/D §8» §b0").setScore(4);
        obj.getScore("§7Münzen §8» §b0").setScore(3);
        obj.getScore("2").setScore(2);
        obj.getScore("1").setScore(1);
        obj.getScore("0").setScore(0);

        // Prefix and Suffix Management
        String rankPrefix = getRankPrefix(p);
        // Suffix für Clan nur setzen, wenn tatsächlich ein Name existiert
        String clanSuffix = (clanName != null && !clanName.trim().isEmpty()) ? " §8[" + clanName + "]" : "";


        // Ensure prefix and suffix do not exceed 16 characters
        rankPrefix = rankPrefix.substring(0, Math.min(16, rankPrefix.length()));
        clanSuffix = clanSuffix.substring(0, Math.min(16, clanSuffix.length()));

        // Team management for player
        Team team = board.getTeam(p.getName().substring(0, Math.min(16, p.getName().length())));
        if (team == null) {
            team = board.registerNewTeam(p.getName().substring(0, Math.min(16, p.getName().length())));
        }

        team.setPrefix(ChatColor.translateAlternateColorCodes('&', rankPrefix));
        team.setSuffix(ChatColor.translateAlternateColorCodes('&', clanSuffix));
        team.addEntry(p.getName());

        // Set the scoreboard to the player
        p.setScoreboard(board);
    }

    private static String getRankPrefix(Player player) {
        if (PermissionsEx.getUser(player).inGroup("Owner")) {
            return "§4Owner §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Entwickler")) {
            return "§3Entwickler §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Admin")) {
            return "§cAdmin §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Architekt")) {
            return "§6Architekt §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Moderator")) {
            return "§5Moderator §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Supporter")) {
            return "§aSupporter §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("ProbeSupporter")) {
            return "§aProbeSupporter §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Emerald")) {
            return "§aEmerald §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Diamond")) {
            return "§bDiamond §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Gold")) {
            return "§6Gold §8» §7";
        } else {
            return "§3Spieler §8» §7";
        }
    }

    public static void removePlayerFromScoreboard(Player player) {
        Scoreboard board = player.getScoreboard();
        Team team = board.getTeam(player.getName().substring(0, Math.min(16, player.getName().length())));
        if (team != null) {
            team.removeEntry(player.getName());
        }
    }

    public static void updatePlayerTeam(Player p) {
        Scoreboard board = p.getScoreboard();
        if (board == null) {
            board = Bukkit.getScoreboardManager().getNewScoreboard();
            p.setScoreboard(board);
        }

        // Clan abrufen
        int clanId = ClanManager.getClanIdByPlayer(p.getUniqueId());
        String clanName = (clanId != -1) ? ClanManager.getClanTag(clanId) : "";

        // Prefix und Suffix neu setzen
        String rankPrefix = getRankPrefix(p);
        String clanSuffix = (clanName != null && !clanName.trim().isEmpty()) ? " §8[" + clanName + "]" : "";

        // Zeichenbegrenzung von 16 einhalten
        rankPrefix = rankPrefix.substring(0, Math.min(16, rankPrefix.length()));
        clanSuffix = clanSuffix.substring(0, Math.min(16, clanSuffix.length()));

        // Team für den Spieler updaten oder neu erstellen
        Team team = board.getTeam(p.getName().substring(0, Math.min(16, p.getName().length())));
        if (team == null) {
            team = board.registerNewTeam(p.getName().substring(0, Math.min(16, p.getName().length())));
        }

        team.setPrefix(ChatColor.translateAlternateColorCodes('&', rankPrefix));
        team.setSuffix(ChatColor.translateAlternateColorCodes('&', clanSuffix));
        team.addEntry(p.getName());
    }


}
