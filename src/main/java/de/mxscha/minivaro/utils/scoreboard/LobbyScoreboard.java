package de.mxscha.minivaro.utils.scoreboard;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.utils.scoreboard.tablist.TablistManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LobbyScoreboard extends ScoreboardBuilder {

    public LobbyScoreboard(Player player) {
        super(player, "       " + MiniVaroCore.getScoreboardTitle() + "       ");
        TablistManager.setTablist(player);
    }

    public void createScoreboard() {
        TeamManager manager = MiniVaroCore.getInstance().getTeamManager();
        if (MiniVaroCore.getInstance().getTeamManager().hasATeam(player.getName())) {
            setScore("§8§m                               ", 12);
            setScore("§8● §7Dein Team§8:", 11);
            setScore("  §8» §9" + manager.getTeam(player.getName()), 10);
            setScore("§a", 9);
            setScore("§8● §7Team Kamerad§8:", 8);
            if (manager.isPlayer1(player.getName())) {
                setScore("  §8» §9" + manager.getPlayer2(manager.getTeam(player.getName())), 7);
            } else {
                setScore("  §8» §9" + manager.getPlayer1(manager.getTeam(player.getName())), 7);
            }
        } else {
            setScore("§8§m                               ", 9);
            setScore("§8● §7Dein Team§8:", 8);
            setScore("  §8» §9Keins", 7);
        }
        setScore("§d", 6);
        setScore("§8● §7Teams§8:", 5);
        setScore("  §8» §9"+manager.getTeamsCount(), 4);
        setScore("§b", 3);
        setScore("§8● §7Spieler Online§8:", 2);
        setScore("  §8» §9"+ Bukkit.getOnlinePlayers().size(), 1);
        setScore("§8§m                               ", 0);
    }

    public void update() {
    }
}
