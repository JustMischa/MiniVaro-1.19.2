package de.mxscha.minivaro.listeners;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.utils.scoreboard.DefaultScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    TeamManager manager;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new DefaultScoreboard(player);
        manager = MiniVaroCore.getInstance().getTeamManager();
        if (manager.hasATeam(player.getName())) {
            event.setJoinMessage("§8[§a" + manager.getTeamShortCut(manager.getTeam(player.getName())) + "§8] §9" + player.getName() + "§7 hat das Spiel betreten!");
        } else {
            event.setJoinMessage("§9" + player.getName() + "§7 hat das Spiel betreten!");
        }
    }
}
