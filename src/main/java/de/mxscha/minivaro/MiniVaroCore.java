package de.mxscha.minivaro;

import de.mxscha.minivaro.commands.TeamCommand;
import de.mxscha.minivaro.database.MySQL;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.listeners.ChatListener;
import de.mxscha.minivaro.listeners.JoinListener;
import de.mxscha.minivaro.listeners.QuitListener;
import de.mxscha.minivaro.listeners.TeamDamageListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiniVaroCore extends JavaPlugin {

    private static String prefix;
    private static String scoreboardTitle;
    private MySQL mySQL;
    private static MiniVaroCore instance;
    TeamManager manager;

    @Override
    public void onEnable() {
        loadConfigs();
        connectToMySQL();
        load(Bukkit.getPluginManager());
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

        getCommand("team").setExecutor(new TeamCommand());

        manager = new TeamManager();
        manager.createTables();
    }

    @Override
    public void onDisable() {

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

    public TeamManager getTeamManager() {
        return manager;
    }
}
