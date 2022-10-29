package de.mxscha.minivaro.utils;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.MySQL;
import de.mxscha.minivaro.database.teams.TeamManager;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class PlayTimeManager {


    private final MySQL mySQL;
    private final int secondsPerDay = 60*20;

    public PlayTimeManager() {
        this.mySQL = MiniVaroCore.getInstance().getMySQL();
        createTable();
    }

    private void createTable() {
        this.mySQL.update("CREATE TABLE IF NOT EXISTS playTime (uuid VARCHAR(36), seconds INT(35))");
    }

    public void initPlayer(UUID uuid, int seconds) {
        if (!doesUserExist(uuid)) {
            mySQL.update("INSERT INTO playTime (uuid, seconds) VALUES (?,?)", uuid.toString(), seconds);
        }
    }

    public boolean doesUserExist(UUID uuid) {
        String qry = "SELECT count(*) AS count FROM playTime WHERE uuid=?";
        try (ResultSet rs = mySQL.query(qry, uuid.toString())) {
            if (rs.next()) {
                return rs.getInt("count") != 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getTime(UUID uuid) {
        String qry = "SELECT seconds FROM playTime WHERE uuid=?";
        try (ResultSet rs = this.mySQL.query(qry, uuid.toString())) {
            if (rs.next()) {
                return rs.getInt("seconds");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void checkResetPlayerTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        String date = format.format(new Date());
        if (date.equalsIgnoreCase("00:00:00")) {
            TeamManager manager = MiniVaroCore.getInstance().getTeamManager();
            PlayTimeManager manager1 = MiniVaroCore.getInstance().getTimeManager();
            for (int i = 1; i <= manager.getPlayerCount(); i++) {
                UUID uuid = UUID.fromString(manager.getPlayerByID(i));
                MySQL mySQL = MiniVaroCore.getInstance().getMySQL();
                if (!manager1.doesUserExist(uuid)) {
                    mySQL.update("INSERT INTO playTime (uuid, seconds) VALUES (?,?)", uuid.toString(), secondsPerDay);
                } else
                    mySQL.update("UPDATE playTime SET seconds=? WHERE uuid=?", getTime(uuid)+secondsPerDay, uuid.toString());
            }
            Bukkit.broadcastMessage(MiniVaroCore.getPrefix()+"§9Die Spielzeit wurde aufgefüllt!");
        }
    }

    public void removeSecond(UUID uuid) {
        mySQL.update("UPDATE playTime SET seconds=? WHERE uuid=?", getTime(uuid)-1, uuid.toString());
    }

    public int getSecondsPerDay() {
        return secondsPerDay;
    }
}
