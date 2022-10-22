package de.mxscha.minivaro.database.teams;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.MySQL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeamManager {

    private MySQL mySQL;

    public void createTables() {
        this.mySQL = MiniVaroCore.getInstance().getMySQL();
        mySQL.update("CREATE TABLE IF NOT EXISTS teams (name VARCHAR(36), player1 VARCHAR(36), player2 VARCHAR(36), kills INT(35))");
    }

    public boolean hasATeam(String name, String playerName) {
        return getPlayer1(name).equals(playerName) || getPlayer2(name).equals(playerName);
    }

    public boolean exists(String name) {
        String qry = "SELECT count(*) AS count FROM teams WHERE name=?";
        try (ResultSet rs = mySQL.query(qry, name)) {
            if (rs.next()) {
                return rs.getInt("count") != 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getPlayer1(String name) {
        String qry = "SELECT player1 FROM teams WHERE name=?";
        try (ResultSet rs = this.mySQL.query(qry, name)) {
            if (rs.next()) {
                return rs.getString("player1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPlayer2(String name) {
        String qry = "SELECT player2 FROM teams WHERE name=?";
        try (ResultSet rs = this.mySQL.query(qry, name)) {
            if (rs.next()) {
                return rs.getString("player2");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getKills(String name) {
        String qry = "SELECT kills FROM teams WHERE name=?";
        try (ResultSet rs = this.mySQL.query(qry, name)) {
            if (rs.next()) {
                return rs.getInt("kills");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
