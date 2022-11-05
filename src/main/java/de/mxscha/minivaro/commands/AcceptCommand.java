package de.mxscha.minivaro.commands;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.Team;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.listeners.lobby.TeamRegisterListener;
import de.mxscha.minivaro.utils.scoreboard.LobbyScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AcceptCommand implements CommandExecutor{

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            switch (args.length) {
                case 0:
                    player.sendMessage(MiniVaroCore.getPrefix()+"§cBitte benutze§8: §7/accept §7(§9playername§7)");
                    break;
                case 1:
                    String req = args[0];
                    TeamManager teamManager = MiniVaroCore.getInstance().getTeamManager();
                    try {
                        Player requestor = Bukkit.getPlayer(req);
                        if (TeamRegisterListener.getPending().containsKey(requestor)) {
                            if (TeamRegisterListener.getPending().get(requestor).equals(player)) {
                                if (!teamManager.hasATeam(player.getName())) {
                                    requestor.sendMessage(MiniVaroCore.getPrefix() + "§9" + player.getName() + "§7 hat deine Einladung §aangenommen§7!");
                                    player.sendMessage(MiniVaroCore.getPrefix() + "§7Du hast die Einladung von §9" + requestor.getName() + "§a angenommen§7!");
                                    int teamID = teamManager.getLastTeamsID();
                                    new Team("Team"+teamID, "#"+teamID, requestor, player).save();
                                    requestor.sendMessage(MiniVaroCore.getPrefix() + "§7Du bist nun im Team §8[§a"+teamManager.getTeam(requestor.getName())+"§8]");
                                    requestor.sendMessage(MiniVaroCore.getPrefix() + "§7Du kannst nun am §bAllay §7oder mit §7/§ateam §eedit §7dein Team bearbeiten!");
                                    player.sendMessage(MiniVaroCore.getPrefix() + "§7Du bist nun im Team §8[§a"+teamManager.getTeam(player.getName())+"§8]");
                                    Bukkit.getOnlinePlayers().forEach(LobbyScoreboard::new);
                                } else
                                    player.sendMessage(MiniVaroCore.getPrefix() + "§cDu bist bereits in einem Team!");
                            } else
                                player.sendMessage(MiniVaroCore.getPrefix() + "§cDu hast von diesem Spieler keine Einladung bekommen!");
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    break;
            }
        }
        return false;
    }
}
