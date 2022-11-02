package de.mxscha.minivaro.utils;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.MySQL;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.utils.scoreboard.DefaultScoreboard;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GameManager {

    private final MySQL mySQL;
    private boolean stopping;

    public GameManager() {
        this.mySQL = MiniVaroCore.getInstance().getMySQL();
        createTable();
    }

    private void createTable() {
        this.mySQL.update("CREATE TABLE IF NOT EXISTS game (started BOOLEAN)");
    }

    private boolean getStarted() {
        String qry = "SELECT started FROM game";
        try (ResultSet rs = this.mySQL.query(qry)) {
            if (rs.next()) {
                return rs.getBoolean("started");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setStarted() {
        mySQL.update("INSERT INTO game (started) VALUES (?)", true);
        Bukkit.getOnlinePlayers().forEach(DefaultScoreboard::new);
        TeamManager manager = MiniVaroCore.getInstance().getTeamManager();
        PlayTimeManager manager1 = MiniVaroCore.getInstance().getTimeManager();
        for (int i = 1; i <= manager.getPlayerCount(); i++) {
            UUID uuid = UUID.fromString(manager.getPlayerByID(i));
            MySQL mySQL = MiniVaroCore.getInstance().getMySQL();
            if (!manager1.doesUserExist(uuid)) {
                mySQL.update("INSERT INTO playTime (uuid, seconds) VALUES (?,?)", uuid.toString(), manager1.getSecondsPerDay());
            } else
                mySQL.update("UPDATE playTime SET seconds=? WHERE uuid=?", manager1.getTime(uuid) + manager1.getSecondsPerDay(), uuid.toString());
        }
        WorldBorder border = Bukkit.getWorld("world").getWorldBorder();
        border.setCenter(Bukkit.getWorld("world").getSpawnLocation());
        border.setSize(3000, 60*4);
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(MiniVaroCore.getPrefix()+ "§c§lDie Border wird nun kleiner!");
                border.setSize(200, 604800*2);
            }
        }.runTaskLater(MiniVaroCore.getInstance(), (20*60)*10);
    }

    public void checkEnd() {
        TeamManager teamManager = MiniVaroCore.getInstance().getTeamManager();
        if (isStarted()) {
            if (!stopping) {
                mySQL.update("UPDATE game (started) VALUES (?)", false);
                if (MiniVaroCore.getInstance().getTeamManager().getAliveTeamsCount() == 1) {
                    stopping = true;
                    new BukkitRunnable() {
                        int seconds = 60*2;
                        @Override
                        public void run() {
                            switch (seconds) {
                                case 120:
                                    for (Player online : Bukkit.getOnlinePlayers()) {
                                        online.sendTitle("§a#" + teamManager.getLastTeamStanding(), "§9hat das Projekt gewonnen!");
                                    }
                                    Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§c§lDas Projekt wurde beendet!");
                                    Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§a#" + teamManager.getLastTeamStanding() + "§9hat das Projekt gewonnen!");
                                    Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§cDer Server wird in §e2 Minuten §causgeschaltet!");
                                    break;
                                case 60:
                                    Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§cDer Server wird in §e1 Minute §causgeschaltet!");
                                    break;
                                case 30: case 15: case 10: case 5: case 4: case 3: case 2:
                                    Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§cDer Server wird in §e"+seconds+" Sekunden §causgeschaltet!");
                                    break;
                                case 1:
                                    Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§cDer Server wird in §e"+seconds+" Sekunde §causgeschaltet!");
                                    break;
                                case 0:
                                    Bukkit.getServer().shutdown();
                                    break;
                            }
                            seconds--;
                        }
                    }.runTaskTimer(MiniVaroCore.getInstance(), 0, 20);
                }
            }

        }
    }

    public boolean isStarted() {
        return getStarted();
    }
}
