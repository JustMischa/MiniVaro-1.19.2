package de.mxscha.minivaro.utils.scoreboard.tablist;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TablistManager {

    public static void setTablist(Player player) {
        setPlayerList(player);
        Bukkit.getOnlinePlayers().forEach(TablistManager::setPlayerTeams);
    }

    private static void setPlayerTeams(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        TeamManager manager = MiniVaroCore.getInstance().getTeamManager();
        Bukkit.getOnlinePlayers().forEach(player1 -> {
            if (manager.hasATeam(player1.getName())) {
                Team all = scoreboard.getTeam("0"+manager.getTeam(player1.getName()));
                if (all == null) {
                    all = scoreboard.registerNewTeam("0"+manager.getTeam(player1.getName()));
                }
                all.setPrefix("§8[§a" + manager.getTeamShortCut(manager.getTeam(player1.getName())) + "§8] §9");
                all.setColor(ChatColor.BLUE);
                all.addEntry(player1.getName());
            } else {
                Team no = scoreboard.getTeam("1no");
                if (no == null) {
                    no = scoreboard.registerNewTeam("1no");
                }
                no.setPrefix("§9");
                no.setColor(ChatColor.BLUE);
                no.addEntry(player1.getName());
            }
        });
    }

    private static void setPlayerList(Player player) {
        player.setPlayerListHeader("§8§m                     §8[ §6" + MiniVaroCore.getScoreboardTitle() + " §8]§m                     " +
                "§f\n §7Lebende Teams§8: §a"+MiniVaroCore.getInstance().getTeamManager().getAliveTeamsCount()+"§7/§c" + MiniVaroCore.getInstance().getTeamManager().getTeamsCount() + "\n");
        player.setPlayerListFooter("\n§fYou§4Tube§8: §7@Mxscha§8, §5Twitch§8: §7@4yMicha\n§8§m                                                           ");
    }
}
