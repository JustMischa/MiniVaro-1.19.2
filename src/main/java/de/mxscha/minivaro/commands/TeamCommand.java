package de.mxscha.minivaro.commands;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.Team;
import de.mxscha.minivaro.database.teams.TeamManager;
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
            switch (args.length) {
                case 2:
                    if (args[0].equalsIgnoreCase("stats")) {
                        String teamName = args[1];
                        TeamManager teamManager = MiniVaroCore.getInstance().getTeamManager();
                        if (teamManager.exists(teamName)) {
                            if (teamManager.isTeamAlive(teamName)) {
                                player.sendMessage("§8§m                   §8[ §6" + teamName + " §8]§m                   ");
                                player.sendMessage(" ");
                                if (teamManager.isAlivePlayer1(teamName)) {
                                    player.sendMessage(" §8| §2Spieler 1§8: §a" + MiniVaroCore.getInstance().getTeamManager().getPlayer1(teamName));
                                } else {
                                    player.sendMessage(" §8| §2Spieler 1§8: §c† §7| §8" + MiniVaroCore.getInstance().getTeamManager().getPlayer1(teamName));
                                }

                                if (teamManager.isAlivePlayer2(teamName)) {
                                    player.sendMessage(" §8| §2Spieler 2§8: §a" + MiniVaroCore.getInstance().getTeamManager().getPlayer2(teamName));
                                } else {
                                    player.sendMessage(" §8| §2Spieler 2§8: §c† §7| §8" + MiniVaroCore.getInstance().getTeamManager().getPlayer2(teamName));
                                }
                                player.sendMessage(" §8| §2Team Tötungen§8: §c" + MiniVaroCore.getInstance().getTeamManager().getKills(teamName));
                                player.sendMessage(" ");
                                player.sendMessage("§8§m                   §8[ §6" + teamName + " §8]§m                   ");
                            } else {
                                player.sendMessage("§8§m                   §8[ §6" + teamName + " §7(§c†§7) §8]§m                   ");
                                player.sendMessage(" ");
                                if (teamManager.isAlivePlayer1(teamName)) {
                                    player.sendMessage(" §8| §2Spieler 1§8: §a" + MiniVaroCore.getInstance().getTeamManager().getPlayer1(teamName));
                                } else {
                                    player.sendMessage(" §8| §2Spieler 1§8: §c† §7| §8" + MiniVaroCore.getInstance().getTeamManager().getPlayer1(teamName));
                                }

                                if (teamManager.isAlivePlayer2(teamName)) {
                                    player.sendMessage(" §8| §2Spieler 2§8: §a" + MiniVaroCore.getInstance().getTeamManager().getPlayer2(teamName));
                                } else {
                                    player.sendMessage(" §8| §2Spieler 2§8: §c† §7| §8" + MiniVaroCore.getInstance().getTeamManager().getPlayer2(teamName));
                                }
                                player.sendMessage(" §8| §2Team Tötungen§8: §c" + MiniVaroCore.getInstance().getTeamManager().getKills(teamName));
                                player.sendMessage(" ");
                                player.sendMessage("§8§m                   §8[ §6" + teamName + " §7(§c†§7) §8]§m                   ");
                            }
                        } else
                            player.sendMessage(MiniVaroCore.getPrefix() + "§cDieses Team existiert nicht!");
                    }
                    break;
                case 5:
                    if (args[0].equalsIgnoreCase("create")) {
                        String teamName = args[1];
                        if (!MiniVaroCore.getInstance().getTeamManager().exists(teamName)) {
                            OfflinePlayer player1 = Bukkit.getOfflinePlayer(args[3]);
                            OfflinePlayer player2 = Bukkit.getOfflinePlayer(args[4]);
                            if (!(player1 == player2)) {
                                if (MiniVaroCore.getInstance().getTeamManager().hasATeam(player1.getName()) && !player2.getName().equals("null")) {
                                    player.sendMessage(MiniVaroCore.getPrefix() + "§cSpieler 1 ist bereits in einem Team");
                                } else if (MiniVaroCore.getInstance().getTeamManager().hasATeam(player2.getName()) && !player2.getName().equals("null")) {
                                    player.sendMessage(MiniVaroCore.getPrefix() + "§cSpieler 2 ist bereits in einem Team");
                                } else {
                                    String shortcut = args[2];
                                    if (shortcut.length() > 5 || shortcut.length() < 2) {
                                        player.sendMessage(MiniVaroCore.getPrefix()+"§cDie Abkürzung muss min 2 und max 5 zeichen haben!");
                                    } else {
                                        new Team(teamName, shortcut, player1, player2).save();
                                        player.sendMessage(MiniVaroCore.getPrefix() + "§7Team §aerstellt§7!");
                                    }
                                }
                            } else {
                                player.sendMessage(MiniVaroCore.getPrefix() + "§cEs müssen unterschiedliche Spieler sein!");
                            }
                        } else {
                            player.sendMessage(MiniVaroCore.getPrefix() + "§cEin Team mit diesem Namen existiert bereits!");
                        }
                    }
                    break;
            }
        }
        return false;
    }
}
