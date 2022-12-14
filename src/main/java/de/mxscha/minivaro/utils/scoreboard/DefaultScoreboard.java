package de.mxscha.minivaro.utils.scoreboard;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.utils.scoreboard.tablist.TablistManager;
import org.bukkit.entity.Player;

public class DefaultScoreboard extends ScoreboardBuilder {


    public DefaultScoreboard(Player player) {
        super(player, "       "+MiniVaroCore.getScoreboardTitle()+"       ");
        TablistManager.setTablist(player);
    }

    public void createScoreboard() {
        TeamManager manager = MiniVaroCore.getInstance().getTeamManager();
        setScore("§8§m                               ", 10);
        setScore("§8● §7Dein Team§8:", 9);
        if (MiniVaroCore.getInstance().getTeamManager().hasATeam(player.getName())) {
            setScore("  §8» §a" + manager.getTeam(player.getName()), 8);
            setScore("§a", 7);
            setScore("§8● §7Team Tötungen§8:", 6);
            setScore("  §8» §9" + manager.getKills(manager.getTeam(player.getName())), 5);
            setScore("§d", 4);
            setScore("§8● §7Team Kamerad§8:", 3);
            if (manager.isPlayer1(player.getName())) {
                if (manager.isAlivePlayer2(manager.getTeam(player.getName()))) {
                    if (!manager.getPlayer2(manager.getTeam(player.getName())).equals("null")) {
                        setScore("  §8» §6" + manager.getPlayer2(manager.getTeam(player.getName())), 2);
                    } else {
                        setScore("  §8» §cNiemand", 2);
                    }
                } else
                    setScore("  §8» §c† §7| §8" + manager.getPlayer2(manager.getTeam(player.getName())), 2);
            } else {
                if (manager.isAlivePlayer1(manager.getTeam(player.getName()))) {
                    if (!manager.getPlayer1(manager.getTeam(player.getName())).equals("null")) {
                        setScore("  §8» §6" + manager.getPlayer1(manager.getTeam(player.getName())), 2);
                    } else {
                        setScore("  §8» §cNiemand", 2);
                    }
                } else
                    setScore("  §8» §c† §7| §8" + manager.getPlayer1(manager.getTeam(player.getName())), 2);
            }
        } else {
            setScore("  §8» §9Keins", 8);
        }
        setScore("§8§m                               ", 1);
    }

    public void update() {
    }
}
