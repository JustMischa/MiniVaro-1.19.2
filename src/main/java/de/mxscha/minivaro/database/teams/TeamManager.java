package de.mxscha.minivaro.database.teams;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TeamManager {

    private MySQL mySQL;

    public void createTables() {
        this.mySQL = MiniVaroCore.getInstance().getMySQL();
        mySQL.update("CREATE TABLE IF NOT EXISTS teams (" +
                "name VARCHAR(36), " +
                "shortcut VARCHAR(6), " +
                "player1 VARCHAR(36), " +
                "player2 VARCHAR(36), " +
                "alivePlayer1 BOOLEAN, " +
                "alivePlayer2 BOOLEAN, " +
                "aliveTeam BOOLEAN, " +
                "kills INT(35)" +
                ")");
        mySQL.update("CREATE TABLE IF NOT EXISTS players(uuid VARCHAR(36), playerID INT(35))");
    }

    // PlayerID manger

    public void initPlayerWithID(UUID uuid) {
        if (!doesPlayerHasID(uuid)) {
            mySQL.update("INSERT INTO players (uuid, playerID) VALUES (?,?)", uuid.toString(), getLastPlayersID()+1);
        }
    }

    public int getLastPlayersID() {
        return getPlayerCount();
    }

    public boolean doesPlayerHasID(UUID uuid) {
        String qry = "SELECT count(*) AS count FROM players WHERE uuid=?";
        try (ResultSet rs = mySQL.query(qry, uuid.toString())) {
            if (rs.next()) {
                return rs.getInt("count") != 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getPlayerByID(int ID) {
        String qry = "SELECT uuid FROM players WHERE playerID=?";
        try (ResultSet rs = this.mySQL.query(qry, ID)) {
            if (rs.next()) {
                 return rs.getString("uuid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getPlayerCount() {
        String qry = "SELECT count(*) AS count FROM players";
        try (ResultSet rs = mySQL.query(qry)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Team Manger

    public void setPlayer1Dead(String teamName) {
        mySQL.update("UPDATE teams SET alivePlayer1=? WHERE name=?", false, teamName);
        if (!isAlivePlayer2(teamName)) {
            setTeamDead(teamName);
        }
    }

    public void setPlayer2Dead(String teamName) {
        mySQL.update("UPDATE teams SET alivePlayer2=? WHERE name=?", false, teamName);
        if (!isAlivePlayer1(teamName)) {
            setTeamDead(teamName);
        }
    }

    private void setTeamDead(String teamName) {
        mySQL.update("UPDATE teams SET aliveTeam=? WHERE name=?", false, teamName);
    }

    public boolean isTeamAlive(String teamName) {
        String qry = "SELECT aliveTeam FROM teams WHERE name=?";
        try (ResultSet rs = this.mySQL.query(qry, teamName)) {
            if (rs.next()) {
                return rs.getBoolean("aliveTeam");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAlivePlayer1(String teamName) {
        String qry = "SELECT alivePlayer1 FROM teams WHERE name=?";
        try (ResultSet rs = this.mySQL.query(qry, teamName)) {
            if (rs.next()) {
                return rs.getBoolean("alivePlayer1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAlivePlayer2(String teamName) {
        String qry = "SELECT alivePlayer2 FROM teams WHERE name=?";
        try (ResultSet rs = this.mySQL.query(qry, teamName)) {
            if (rs.next()) {
                return rs.getBoolean("alivePlayer2");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getTeamShortCut(String teamName) {
        String qry = "SELECT shortcut FROM teams WHERE name=?";
        try (ResultSet rs = this.mySQL.query(qry, teamName)) {
            if (rs.next()) {
                return rs.getString("shortcut");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTeam(String playerName) {
        if (isPlayer1(playerName)) {
            String qry = "SELECT name FROM teams WHERE player1=?";
            try (ResultSet rs = this.mySQL.query(qry, playerName)) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (isPlayer2(playerName)) {
            String qry = "SELECT name FROM teams WHERE player2=?";
            try (ResultSet rs = this.mySQL.query(qry, playerName)) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean exists(String TeamName) {
        String qry = "SELECT count(*) AS count FROM teams WHERE name=?";
        try (ResultSet rs = mySQL.query(qry, TeamName)) {
            if (rs.next()) {
                return rs.getInt("count") != 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getPlayer1(String TeamName) {
        String qry = "SELECT player1 FROM teams WHERE name=?";
        try (ResultSet rs = this.mySQL.query(qry, TeamName)) {
            if (rs.next()) {
                return rs.getString("player1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPlayer2(String TeamName) {
        String qry = "SELECT player2 FROM teams WHERE name=?";
        try (ResultSet rs = this.mySQL.query(qry, TeamName)) {
            if (rs.next()) {
                return rs.getString("player2");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getKills(String TeamName) {
        String qry = "SELECT kills FROM teams WHERE name=?";
        try (ResultSet rs = this.mySQL.query(qry, TeamName)) {
            if (rs.next()) {
                return rs.getInt("kills");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean hasATeam(String playerName) {
        return isPlayer1(playerName) || isPlayer2(playerName);
    }

    public boolean isInTeam(String TeamName, String playerName) {
        if (exists(TeamName)) {
            return getPlayer1(TeamName).equals(playerName) || getPlayer2(TeamName).equals(playerName);
        } else
            return false;
    }

    public int getTeamsCount() {
        String qry = "SELECT count(*) AS count FROM teams";
        try (ResultSet rs = mySQL.query(qry)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getAliveTeamsCount() {
        String qry = "SELECT count(*) AS count FROM teams WHERE aliveTeam=?";
        try (ResultSet rs = mySQL.query(qry, true)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean isInSameTeam(String player1, String player2) {
        return getTeam(player1).equals(getTeam(player2));
    }

    public boolean isPlayer1(String playerName) {
        String qry = "SELECT count(*) AS count FROM teams WHERE player1=?";
        try (ResultSet rs = mySQL.query(qry, playerName)) {
            if (rs.next()) {
                return rs.getInt("count") != 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPlayer2(String playerName) {
        String qry1 = "SELECT count(*) AS count FROM teams WHERE player2=?";
        try (ResultSet rs = mySQL.query(qry1, playerName)) {
            if (rs.next()) {
                return rs.getInt("count") != 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addKill(String teamName) {
        mySQL.update("UPDATE teams SET kills=? WHERE name=?", 1, teamName);
    }
}
