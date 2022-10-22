package de.mxscha.minivaro.database.teams;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.MySQL;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Team {

    private final MySQL mySQL;
    private final String name;
    private final OfflinePlayer player1;
    private final OfflinePlayer player2;

    public Team(String name, OfflinePlayer player1, OfflinePlayer player2) {
        this.mySQL = MiniVaroCore.getInstance().getMySQL();
        this.name = name;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void save() {
        mySQL.update("INSERT INTO teams (name, player1, player2, kills) VALUES (?,?,?,?)", name, player1.getName(), player2.getName(), 0);
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
