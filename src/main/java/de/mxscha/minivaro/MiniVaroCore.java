package de.mxscha.minivaro;

import de.mxscha.minivaro.commands.AdminStartCommand;
import de.mxscha.minivaro.commands.TeamCommand;
import de.mxscha.minivaro.database.MySQL;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.listeners.*;
import de.mxscha.minivaro.listeners.lobby.LobbyListener;
import de.mxscha.minivaro.utils.GameManager;
import de.mxscha.minivaro.utils.PlayTimeManager;
import de.mxscha.minivaro.utils.timer.GameStartCountdown;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class MiniVaroCore extends JavaPlugin {

    // TODO: eliminations -> Kick = nicht mehr joinen
    //                    -> In Database eintragen
    //       Spawns       -> Setup command
    //                    -> vorspiel phase
    //       Worldborder  -> verkleinert sich nach X tagen
    //              -> Beide Spieler MÜSSEN gleichzeitig spielen

    private static String prefix;
    private static String scoreboardTitle;
    private MySQL mySQL;
    private static MiniVaroCore instance;
    TeamManager TeamManager;
    GameManager GameManager;
    GameStartCountdown countdown;
    PlayTimeManager TimeManager;

    @Override
    public void onEnable() {
        loadConfigs();
        connectToMySQL();
        load(Bukkit.getPluginManager());
        removePlayTime();
    }

    private void loadConfigs() {
        createConfigDefaults();
        saveConfig();
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

        getCommand("team").setExecutor(new TeamCommand());
        getCommand("start").setExecutor(new AdminStartCommand());

        TeamManager = new TeamManager();
        GameManager = new GameManager();
        countdown = new GameStartCountdown();
        TimeManager = new PlayTimeManager();
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
                            case 60 -> player.sendMessage(MiniVaroCore.getPrefix() + "§c§lACHTUNG§7, §7Deine Zeit für heute endet in §91 Minute");
                            case 30, 20, 15, 10, 5, 4, 3, 2 -> player.sendMessage(MiniVaroCore.getPrefix() + "§c§lACHTUNG§7, §7Deine Zeit für heute endet in §9" + TimeManager.getTime(player.getUniqueId()) + " Sekunden");
                            case 1 -> player.sendMessage(MiniVaroCore.getPrefix() + "§c§lACHTUNG§7, §7Deine Zeit für heute endet in §9" + TimeManager.getTime(player.getUniqueId()) + " Sekunde");
                            case 0 -> player.kickPlayer(MiniVaroCore.getScoreboardTitle() + "\n §cDeine Zeit ist aufgebraucht! \n §9Deine Zeit wird um Mitternacht wieder aufgefüllt!");
                        }
                        // need for every player not for every online player!
                        TimeManager.checkResetPlayerTime(player.getUniqueId());
                    });
                }
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
