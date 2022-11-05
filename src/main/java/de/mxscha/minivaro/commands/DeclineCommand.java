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

public class DeclineCommand implements CommandExecutor{

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            switch (args.length) {
                case 0:
                    player.sendMessage(MiniVaroCore.getPrefix()+"§cBitte benutze§8: §7/decline §7(§9playername§7)");
                    break;
                case 1:
                    String req = args[0];
                    TeamManager teamManager = MiniVaroCore.getInstance().getTeamManager();
                    try {
                        Player requestor = Bukkit.getPlayer(req);
                        if (TeamRegisterListener.getPending().containsKey(requestor)) {
                            if (TeamRegisterListener.getPending().get(requestor).equals(player)) {
                                TeamRegisterListener.getPending().remove(requestor);
                                player.sendMessage(MiniVaroCore.getPrefix()+"§7Du hast die Einladung von §9" + requestor.getName() + "§c abgelehnt§7!");
                                requestor.sendMessage(MiniVaroCore.getPrefix() + "§9" + player.getName()+ "§7hat deine Einladung §cabgelehnt§7!");
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
