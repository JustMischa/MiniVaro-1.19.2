package de.mxscha.minivaro.database.teams;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.MySQL;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class Team {

    private final MySQL mySQL;
    private final String name;
    private final String shortcut;
    private final OfflinePlayer player1;
    private final OfflinePlayer player2;

    public Team(String name, String shortcut, OfflinePlayer player1, OfflinePlayer player2) {
        this.mySQL = MiniVaroCore.getInstance().getMySQL();
        this.name = name;
        this.shortcut = shortcut;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void save() {
        TeamManager teamManager = MiniVaroCore.getInstance().getTeamManager();
        mySQL.update("INSERT INTO teams (name, shortcut, player1, player2, alivePlayer1, alivePlayer2, aliveTeam, kills, teamID) VALUES (?,?,?,?,?,?,?,?, ?)",
                name, shortcut, player1.getName(), player2.getName(), true, true, true, 0, teamManager.getLastTeamsID());
        if (player1.getName().equals("null")) {
            teamManager.initPlayerWithID(UUID.randomUUID());
        } else {
            teamManager.initPlayerWithID(player1.getUniqueId());
        }
        if (player2.getName().equals("null")) {
            teamManager.initPlayerWithID(UUID.randomUUID());
        } else {
            teamManager.initPlayerWithID(player2.getUniqueId());
        }
    }

    public String getShortcut() {
        return shortcut;
    }

    public String getName() {
        return name;
    }

    public OfflinePlayer getPlayer1() {
        return player1;
    }

    public OfflinePlayer getPlayer2() {
        return player2;
    }
}
