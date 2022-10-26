package de.mxscha.minivaro.utils;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.MySQL;
import de.mxscha.minivaro.utils.scoreboard.DefaultScoreboard;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        Bukkit.getOnlinePlayers().forEach(player -> {
            MiniVaroCore.getInstance().getTimeManager().initPlayer(player.getUniqueId(), MiniVaroCore.getInstance().getTimeManager().getSecondsPerDay());
            new DefaultScoreboard(player);
        });
    }

    public boolean isStarted() {
        return getStarted();
    }
}
