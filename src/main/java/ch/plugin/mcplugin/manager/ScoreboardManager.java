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
        String clanSuffix = (clanName != null) ? " §8[" + clanName + "]" : "";

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
            return "§4Owner §8× §7";
        } else if (PermissionsEx.getUser(player).inGroup("Entwickler")) {
            return "§8× §3Entwickler §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Admin")) {
            return "§8× §cAdmin §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Architekt")) {
            return "§8× §6Architekt §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Moderator")) {
            return "§8× §5Moderator §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Supporter")) {
            return "§8× §aSupporter §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("ProbeSupporter")) {
            return "§8× §aProbeSupporter §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Emerald")) {
            return "§8× §aEmerald §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Diamond")) {
            return "§8× §bDiamond §8» §7";
        } else if (PermissionsEx.getUser(player).inGroup("Gold")) {
            return "§8× §6Gold §8» §7";
        } else {
            return "§8× §3Spieler §8» §7";
        }
    }

    public static void removePlayerFromScoreboard(Player player) {
        Scoreboard board = player.getScoreboard();
        Team team = board.getTeam(player.getName().substring(0, Math.min(16, player.getName().length())));
        if (team != null) {
            team.removeEntry(player.getName());
        }
    }
}
