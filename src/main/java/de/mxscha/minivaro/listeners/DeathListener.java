package de.mxscha.minivaro.listeners;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.utils.scoreboard.DefaultScoreboard;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.nio.Buffer;
import java.util.ArrayList;

public class DeathListener implements Listener {

    private static final ArrayList<Player> eliminated = new ArrayList<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        TeamManager manager = MiniVaroCore.getInstance().getTeamManager();
        if (MiniVaroCore.getInstance().getGameManager().isStarted()) {
            if (player.getKiller() != null) {
                Player killer = player.getKiller();
                if (manager.hasATeam(player.getName())) {
                    if (manager.hasATeam(killer.getName())) {
                        event.setDeathMessage(MiniVaroCore.getPrefix() + "§8[§a"+manager.getTeamShortCut(manager.getTeam(player.getName()))+"§8] §9"
                                +player.getName() + "§7 wurde von §8[§a"+manager.getTeamShortCut(manager.getTeam(killer.getName()))+"§8] §c"
                                +killer.getName() + "§7 geötet!");
                        eliminated.add(player);
                        manager.addKill(manager.getTeam(killer.getName()));
                        Bukkit.getOnlinePlayers().forEach(online -> {
                            online.playSound(online.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
                            new DefaultScoreboard(online);
                        });
                        player.kickPlayer(MiniVaroCore.getScoreboardTitle() + "\n §8§m                                                 §f\n" +
                                "  §c§lDu bist ausgeschieden! \n \n  §7Vielen dank fürs mitmachen! \n  §8§m                                                 §f");
                        killPlayerInDatabase(player);
                    }
                }
            } else {
                event.setDeathMessage(MiniVaroCore.getPrefix() + "§8[§a"+manager.getTeamShortCut(manager.getTeam(player.getName()))+"§8] §9"
                        +player.getName() + "§7 ist gestorben!");
                eliminated.add(player);
                Bukkit.getOnlinePlayers().forEach(online -> {
                    online.playSound(online.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
                    new DefaultScoreboard(online);
                });
                player.kickPlayer(MiniVaroCore.getScoreboardTitle() + "\n §8§m                                                 §f\n" +
                        "  §c§lDu bist ausgeschieden! \n \n  §7Vielen dank fürs mitmachen! \n  §8§m                                                 §f");
                killPlayerInDatabase(player);

            }
        }
    }

    private void killPlayerInDatabase(Player player) {
        TeamManager manager = MiniVaroCore.getInstance().getTeamManager();
        if (manager.isPlayer1(player.getName())) {
            manager.setPlayer1Dead(manager.getTeam(player.getName()));
        } else {
            manager.setPlayer2Dead(manager.getTeam(player.getName()));
        }
    }

    public static ArrayList<Player> getEliminated() {
        return eliminated;
    }
}
