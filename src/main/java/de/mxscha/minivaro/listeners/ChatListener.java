package de.mxscha.minivaro.listeners;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.TeamManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.ChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        TeamManager manager = MiniVaroCore.getInstance().getTeamManager();
        if (manager.hasATeam(player.getName())) {
            event.setFormat("§8[§a"+manager.getTeamShortCut(manager.getTeam(player.getName()))+"§8] §9" + player.getName() + " §8» §7§o" + event.getMessage());
        } else
            event.setFormat("§9" + player.getName() + " §8» §7§o" + event.getMessage());
    }
}
