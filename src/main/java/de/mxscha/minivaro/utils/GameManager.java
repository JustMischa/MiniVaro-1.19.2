package de.mxscha.minivaro.utils;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.MySQL;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.utils.scoreboard.DefaultScoreboard;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GameManager {

    private final MySQL mySQL;

    public GameManager() {
        this.mySQL = MiniVaroCore.getInstance().getMySQL();
        createTable();
    }

    private void createTable() {
        this.mySQL.update("CREATE TABLE IF NOT EXISTS game (started BOOLEAN)");
    }

    private boolean getStarted() {
        String qry = "SELECT started FROM game WHERE started=?";
        try (ResultSet rs = this.mySQL.query(qry, true)) {
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
    }

    public boolean isStarted() {
        return getStarted();
    }
}
