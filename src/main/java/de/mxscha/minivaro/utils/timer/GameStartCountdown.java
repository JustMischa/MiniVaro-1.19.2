package de.mxscha.minivaro.utils.timer;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.listeners.JoinListener;
import de.mxscha.minivaro.listeners.QuitListener;
import de.mxscha.minivaro.utils.location.ConfigLocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GameStartCountdown {

    int seconds = 60;
    int DefaultSeconds = 60;
    BukkitTask id;
    boolean isRunning;

    public void start() {
        for(Player online : Bukkit.getOnlinePlayers()) {
            online.sendTitle("§c§lACHTUNG", "§9Der Countdown wurde gestartet!");
        }
        if (!isRunning) {
            isRunning = true;
            id = new BukkitRunnable() {
                @Override
                public void run() {
                    for(Player online : Bukkit.getOnlinePlayers()) {
                        online.setLevel(seconds);
                        online.setTotalExperience(DefaultSeconds);
                    }
                    switch (seconds) {
                        case 300:
                             Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§7Das Projekt startet in §9§l5 Minuten§7!");
                            for(Player online : Bukkit.getOnlinePlayers()) {
                                online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 25, 0);
                            }
                            break;
                        case 240:
                            Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§7Das Projekt startet in §9§l4 Minuten§7!");
                            for(Player online : Bukkit.getOnlinePlayers()) {
                                online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 25, 0);
                            }
                            break;
                        case 120:
                            Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§cTeleportiere...");
                            Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§7Das Projekt startet in §9§l2 Minuten§7!");
                            for(Player online : Bukkit.getOnlinePlayers()) {
                                online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 25, 0);
                            }
                            break;
                        case 60: case 30: case 20: case 15: case 10: case 9: case 8: case 7: case 6: case 5: case 4: case 3: case 2:
                            Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§7Das Projekt startet in §9§l"+seconds+" Sekunden§7!");
                            for(Player online : Bukkit.getOnlinePlayers()) {
                                online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 25, 0);
                            }
                            break;
                        case 40:
                            for(Player online : Bukkit.getOnlinePlayers()) {
                                online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 25, 0);
                                sendTeleportTitle(online);
                            }
                            break;
                        case 1:
                            Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§7Das Projekt startet in §9§l"+seconds+" Sekunde§7!");
                            for(Player online : Bukkit.getOnlinePlayers()) {
                                online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 25, 0);
                            }
                            break;
                        case 0:
                            stop();
                            MiniVaroCore.getInstance().getGameManager().setStarted();
                            Bukkit.broadcastMessage(MiniVaroCore.getPrefix() + "§c§lACHTUNG§7, §7§lDas Projekt beginnt nun!");
                            break;
                    }
                    seconds--;
                }
            }.runTaskTimer(MiniVaroCore.getInstance(), 0, 20);
        }
    }


    private void sendTeleportTitle(Player online) {
        new BukkitRunnable() {
            int teleportseconds = 5;
            @Override
            public void run() {
                switch (teleportseconds) {
                    case 5:
                        online.sendTitle("", "§cTeleportiere");
                        break;
                    case 4:
                        online.sendTitle("", "§cTeleportiere.");
                        break;
                    case 3:
                        online.sendTitle("", "§cTeleportiere..");
                        break;
                    case 2:
                        online.sendTitle("", "§cTeleportiere...");
                        break;
                    case 1:
                        teleport(online);
                        online.playSound(online.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 25, 0);
                        break;
                    case 0:
                        cancel();
                        break;
                }
                teleportseconds--;
            }
        }.runTaskTimer(MiniVaroCore.getInstance(), 0, 20);
    }

    private void teleport(Player online) {
        WorldBorder border = Bukkit.getWorld("world").getWorldBorder();
        border.setCenter(Bukkit.getWorld("world").getSpawnLocation());
        border.setSize(300);
        TeamManager teamManager = MiniVaroCore.getInstance().getTeamManager();
        if (teamManager.hasATeam(online.getName())) {
            int id = teamManager.getPlayersID(online.getUniqueId());
            Location spawn = new ConfigLocationUtil("Spawn"+id).loadLocation();
            if (spawn == null) return;
            online.teleport(spawn);
        } else {
            JoinListener.noTeam.add(online);
            online.kickPlayer(MiniVaroCore.getScoreboardTitle() + "\n §cDu bist nicht berechtigt \n §cAuf diesem Server zu sein!");
        }
    }

    public void stop() {
        id.cancel();
        id = null;
        seconds = DefaultSeconds;
        isRunning = false;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }

    public BukkitTask getId() {
        return id;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
