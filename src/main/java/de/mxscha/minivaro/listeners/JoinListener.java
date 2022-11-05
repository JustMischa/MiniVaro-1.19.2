package de.mxscha.minivaro.listeners;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.utils.PlayTimeManager;
import de.mxscha.minivaro.utils.location.ConfigLocationUtil;
import de.mxscha.minivaro.utils.scoreboard.DefaultScoreboard;
import de.mxscha.minivaro.utils.scoreboard.LobbyScoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

public class JoinListener implements Listener {

    TeamManager manager;
    public static ArrayList<Player> noTime = new ArrayList<>();
    public static ArrayList<Player> noTeam = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        manager = MiniVaroCore.getInstance().getTeamManager();
        if (manager.hasATeam(player.getName())) {
            player.sendMessage(manager.getTeamID(manager.getTeam(player.getName()))+"");
            event.setJoinMessage("§8[§a" + manager.getTeamShortCut(manager.getTeam(player.getName())) + "§8] §9" + player.getName() + "§7 hat das Spiel betreten!");
        } else {
            event.setJoinMessage("§9" + player.getName() + "§7 hat das Spiel betreten!");
        }
        if (MiniVaroCore.getInstance().getGameManager().isStarted()) {
            PlayTimeManager timeManager = MiniVaroCore.getInstance().getTimeManager();
            if (timeManager.getTime(player.getUniqueId()) != 0) {
                if (manager.isPlayer1(player.getName())) {
                    if (!manager.isAlivePlayer1(manager.getTeam(player.getName()))) {
                        DeathListener.getEliminated().add(player);
                        player.kickPlayer(MiniVaroCore.getScoreboardTitle() + "\n §8§m                                                 §f\n" +
                                "  §c§lDu bist bereits ausgeschieden! \n \n  §7Vielen dank fürs mitmachen! \n  §8§m                                                 §f");
                    }
                } else {
                    if (!manager.isAlivePlayer2(manager.getTeam(player.getName()))) {
                        DeathListener.getEliminated().add(player);
                        player.kickPlayer(MiniVaroCore.getScoreboardTitle() + "\n §8§m                                                 §f\n" +
                                "  §c§lDu bist bereits ausgeschieden! \n \n  §7Vielen dank fürs mitmachen! \n  §8§m                                                 §f");
                    }
                }
                if (manager.hasATeam(player.getName())) {
                    protect(player);
                    new DefaultScoreboard(player);
                } else {
                    noTeam.add(player);
                    event.setJoinMessage(null);
                    player.kickPlayer(MiniVaroCore.getScoreboardTitle() + "\n §cDu bist nicht berechtigt \n §cDiesen Server zu betreten!");
                }
            } else {
                noTime.add(player);
                player.kickPlayer(MiniVaroCore.getScoreboardTitle() + "\n §cDeine Zeit ist aufgebraucht! \n " +
                        "§9Deine Zeit wird um Mitternacht wieder aufgefüllt!");
                event.setJoinMessage(null);
            }
        } else {
            Bukkit.getOnlinePlayers().forEach(LobbyScoreboard::new);
            Location location = new ConfigLocationUtil("Lobby").loadLocation();
            if (location == null)
                return;
            player.teleport(location);
            if (MiniVaroCore.getInstance().getCountdown().isRunning()) {
                if (!manager.hasATeam(player.getName())) {
                    noTeam.add(player);
                    event.setJoinMessage(null);
                    player.kickPlayer(MiniVaroCore.getScoreboardTitle() + "\n §cDu bist nicht berechtigt \n §cDiesen Server zu betreten!");
                }
            }
        }
    }

    private void protect(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                ProtectionTimeListener.getProtected().add(player);
                player.sendMessage(MiniVaroCore.getPrefix() + "§c§lACHTUNG§7, §7Du bist in §930 Sekunden §7verwundbar!");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ProtectionTimeListener.getProtected().remove(player);
                        player.sendMessage(MiniVaroCore.getPrefix() + "§c§lACHTUNG§7, §cDu bist nun verwundbar!");
                    }
                }.runTaskLater(MiniVaroCore.getInstance(), 20*30);
            }
        }.runTaskLater(MiniVaroCore.getInstance(), 5);
    }
}
