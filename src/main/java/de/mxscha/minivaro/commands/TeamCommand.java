package de.mxscha.minivaro.commands;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("stats")) {
                    String teamName = args[1];
                    if (MiniVaroCore.getInstance().getManager().exists(teamName)) {
                        player.sendMessage("§8§m                   §8[ §6" + teamName + " §8]§m                   ");
                        player.sendMessage(" ");
                        player.sendMessage(" §8| §2Spieler 1§8: §a" + MiniVaroCore.getInstance().getManager().getPlayer1(teamName));
                        player.sendMessage(" §8| §2Spieler 2§8: §a" + MiniVaroCore.getInstance().getManager().getPlayer2(teamName));
                        player.sendMessage(" §8| §2Team Tötungen§8: §c" + MiniVaroCore.getInstance().getManager().getKills(teamName));
                        player.sendMessage(" ");
                        player.sendMessage("§8§m                   §8[ §6" + teamName + " §8]§m                   ");
                    } else
                        player.sendMessage(MiniVaroCore.getPrefix() + "§cDieses Team existiert nicht!");
                }
            }
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("create")) {
                    String teamName = args[1];
                    if (!MiniVaroCore.getInstance().getManager().exists(teamName)) {
                        OfflinePlayer player1 = Bukkit.getOfflinePlayer(args[2]);
                        OfflinePlayer player2 = Bukkit.getOfflinePlayer(args[3]);
                        if (!(player1 == player2)) {
                            if (MiniVaroCore.getInstance().getManager().hasATeam(teamName, player1.getName())) {
                                player.sendMessage(MiniVaroCore.getPrefix() + "§cSpieler 1 ist bereits in einem Team");
                            } else if (MiniVaroCore.getInstance().getManager().hasATeam(teamName, player2.getName())) {
                                player.sendMessage(MiniVaroCore.getPrefix() + "§cSpieler 2 ist bereits in einem Team");
                            } else {
                                new Team(teamName, player1, player2).save();
                                player.sendMessage(MiniVaroCore.getPrefix() + "§7Team §aerstellt§7!");
                            }
                        } else {
                            player.sendMessage(MiniVaroCore.getPrefix() + "§cEs müssen unterschiedliche Spieler sein!");
                        }
                    } else {
                        player.sendMessage(MiniVaroCore.getPrefix() + "§cEin Team mit diesem Namen existiert bereits!");
                    }
                }
            }
        }
        return false;
    }
}
