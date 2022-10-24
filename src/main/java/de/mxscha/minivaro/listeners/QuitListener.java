package de.mxscha.minivaro.listeners;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.utils.scoreboard.DefaultScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    TeamManager manager;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        manager = MiniVaroCore.getInstance().getTeamManager();
        if (manager.hasATeam(player.getName())) {
            event.setQuitMessage("§8[§a" + manager.getTeamShortCut(manager.getTeam(player.getName())) + "§8] §9" + player.getName() + "§7 hat das Spiel verlassen!");
        } else {
            event.setQuitMessage("§9" + player.getName() + "§7 hat das Spiel verlassen!");
        }
    }
}
