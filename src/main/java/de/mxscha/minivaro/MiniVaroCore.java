package de.mxscha.minivaro;

import de.mxscha.minivaro.commands.*;
import de.mxscha.minivaro.database.MySQL;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.listeners.*;
import de.mxscha.minivaro.listeners.lobby.LobbyListener;
import de.mxscha.minivaro.listeners.lobby.TeamRegisterListener;
import de.mxscha.minivaro.utils.ChestManager;
import de.mxscha.minivaro.utils.GameManager;
import de.mxscha.minivaro.utils.PlayTimeManager;
import de.mxscha.minivaro.utils.location.LocationsConfig;
import de.mxscha.minivaro.utils.timer.GameStartCountdown;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class MiniVaroCore extends JavaPlugin {

    // 36 Players = 18 Teams

    private static String prefix;
    private static String scoreboardTitle;
    private MySQL mySQL;
    private static MiniVaroCore instance;
    TeamManager TeamManager;
    GameManager GameManager;
    GameStartCountdown countdown;
    PlayTimeManager TimeManager;
    TeamRegisterListener teamRegister;

    @Override
    public void onEnable() {
        loadConfigs();
        connectToMySQL();
        load(Bukkit.getPluginManager());
        removePlayTime();
        teamRegister.spawnTeamRegisterEntity();
    }

    private void loadConfigs() {
        createConfigDefaults();
        saveConfig();
        LocationsConfig.save();
    }

    private void load(PluginManager pluginManager) {
        instance = this;
        prefix = getConfig().getString("Messages.Prefix");
        scoreboardTitle = getConfig().getString("Messages.ScoreboardTitle");

        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new QuitListener(), this);
        pluginManager.registerEvents(new ChatListener(), this);
        pluginManager.registerEvents(new TeamDamageListener(), this);
        pluginManager.registerEvents(new DeathListener(), this);
        pluginManager.registerEvents(new ProtectionTimeListener(), this);
        pluginManager.registerEvents(new LobbyListener(), this);
        pluginManager.registerEvents(new ChestManager(), this);
        pluginManager.registerEvents(new TeamRegisterListener(), this);

        getCommand("team").setExecutor(new TeamCommand());
        getCommand("start").setExecutor(new AdminStartCommand());
        getCommand("setup").setExecutor(new AdminSetupCommand());
        getCommand("admin").setExecutor(new AdminCommand());
        getCommand("accept").setExecutor(new AcceptCommand());
        getCommand("decline").setExecutor(new DeclineCommand());

        dropTables();

        TeamManager = new TeamManager();
        GameManager = new GameManager();
        countdown = new GameStartCountdown();
        TimeManager = new PlayTimeManager();
        teamRegister = new TeamRegisterListener();
        TeamManager.createTables();
    }

    @Override
    public void onDisable() {

    }

    private void removePlayTime() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (GameManager.isStarted()) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        TimeManager.removeSecond(player.getUniqueId());
                        switch (TimeManager.getTime(player.getUniqueId())) {
                            case 60 -> Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§c" + player.getName() + "§7 wird in §9§l1 Minute §7gekickt!");
                            case 30, 20, 15, 10, 5, 4, 3, 2 -> Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§c" + player.getName() + "§7 wird in §9§l"+TimeManager.getTime(player.getUniqueId())+" Sekunden §7gekickt!");
                            case 1 -> Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§c" + player.getName() + "§7 wird in §9§l"+TimeManager.getTime(player.getUniqueId())+" Sekunde §7gekickt!");
                            case 0 -> player.kickPlayer(MiniVaroCore.getScoreboardTitle() + "\n §8§m                                                 §f\n" + "  §c§lDeine Zeit für Heute ist aufgebraucht! \n \n  §7Du kannst morgen wieder Spielen! \n  §8§m                                                 §f");
                        }
                    });
                }
                TimeManager.checkResetPlayerTime();
                GameManager.checkEnd();
            }
        }.runTaskTimer(this, 0, 20);
    }

    private void createConfigDefaults() {
        if (!getConfig().contains("Messages.Prefix")) {
            getConfig().set("Messages.Prefix", "§6§lMini§a§lVaro §8| §f");
            getConfig().set("Messages.ScoreboardTitle", "§6§lMini§a§lVaro");
        }
        if (!getConfig().contains("MySQL.url")) {
            getConfig().set("MySQL.url", "url");
            getConfig().set("MySQL.port", "port");
            getConfig().set("MySQL.database", "database");
            getConfig().set("MySQL.user", "user");
            getConfig().set("MySQL.password", "password");
        }
        saveConfig();
    }

    private void connectToMySQL() {
        String url = "";
        int port = 0;
        String database = "";
        String user = "";
        String password = "";
        try {
            url = getConfig().getString("MySQL.url");
            port = getConfig().getInt("MySQL.port");
            database = getConfig().getString("MySQL.database");
            user = getConfig().getString("MySQL.user");
            password = getConfig().getString("MySQL.password");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(prefix + "Verbinden der Daten ist etwas schiefgelaufen!");
        }
        if (password == null)
            password = "";
        this.mySQL = MySQL.newBuilder().withUrl(url).withPort(port).withDatabase(database).withUser(user).withPassword(password).create();
    }

    private void dropTables() {
        mySQL.update("DROP TABLE players");
        mySQL.update("DROP TABLE playTime");
        mySQL.update("DROP TABLE teams");
        mySQL.update("DROP TABLE game");
    }

    public static String getPrefix() {
        return prefix;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public static MiniVaroCore getInstance() {
        return instance;
    }

    public static String getScoreboardTitle() {
        return scoreboardTitle;
    }

    public GameStartCountdown getCountdown() {
        return countdown;
    }

    public PlayTimeManager getTimeManager() {
        return TimeManager;
    }

    public TeamManager getTeamManager() {
        return TeamManager;
    }

    public GameManager getGameManager() {
        return GameManager;
    }
}
