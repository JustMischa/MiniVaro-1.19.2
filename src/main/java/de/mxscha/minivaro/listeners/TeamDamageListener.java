package de.mxscha.minivaro.listeners;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TeamDamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getDamager() instanceof Player attacker) {
                TeamManager manager = MiniVaroCore.getInstance().getTeamManager();
                if (manager.hasATeam(player.getName())) {
                    if (manager.hasATeam(attacker.getName())) {
                        if (manager.isInSameTeam(player.getName(), attacker.getName())) {
                            event.setDamage(0);
                        }
                    }
                }
            }
        }
    }
}