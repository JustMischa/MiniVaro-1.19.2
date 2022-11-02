package de.mxscha.minivaro.commands;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.utils.location.ConfigLocationUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminSetupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("varo.setup")) {
                switch (args.length) {
                    case 0:
                        break;
                    case 2:
                        if (args[0].equalsIgnoreCase("set")) {
                            if (args[1].equalsIgnoreCase("lobby")) {
                                new ConfigLocationUtil(player.getLocation(),"Lobby").saveLocation();
                                player.sendMessage(MiniVaroCore.getPrefix() + "§7Du hast den Lobby Spawn gesetzt!");
                            }
                        }
                        break;
                    case 3:
                        if (args[0].equalsIgnoreCase("set")) {
                            if (args[1].equalsIgnoreCase("Spawn")) {
                                try {
                                    int spawn = Integer.parseInt(args[2]);
                                    new ConfigLocationUtil(player.getLocation(), "Spawn"+spawn).saveLocation();
                                    player.sendMessage(MiniVaroCore.getPrefix() + "§7Du hast den §9"+spawn+" §7Spawn gesetzt!");
                                } catch (NumberFormatException exception) {
                                    player.sendMessage(MiniVaroCore.getPrefix() + "§cDer Spawn name muss eine Zahl sein!");
                                }
                            }
                        }
                        if (args[0].equalsIgnoreCase("set")) {
                            if (args[1].equalsIgnoreCase("lobby")) {
                                if (args[2].equalsIgnoreCase("register")) {
                                    new ConfigLocationUtil(player.getLocation(),"LobbyTeamRegister").saveLocation();
                                    player.sendMessage(MiniVaroCore.getPrefix() + "§7Du hast den Lobby Team Register gesetzt!");
                                }
                            }
                        }
                        break;
                    case 5:
                        if (args[0].equalsIgnoreCase("set")) {
                            if (args[1].equalsIgnoreCase("Region")) {
                                if (args[2].equalsIgnoreCase("Portal")) {
                                    if (args[3].equalsIgnoreCase("1")) {
                                        new ConfigLocationUtil(player.getLocation(),"PortalArea1").saveLocation();
                                        player.sendMessage(MiniVaroCore.getPrefix() + "§7Du hast die 1 Location für die Portal-Chest-Area gesetzt!");
                                    } else if (args[3].equalsIgnoreCase("2")) {
                                        new ConfigLocationUtil(player.getLocation(),"PortalArea2").saveLocation();
                                        player.sendMessage(MiniVaroCore.getPrefix() + "§7Du hast die 1 Location für die Portal-Chest-Area gesetzt!");
                                    }
                                }
                            }
                        }
                        break;
                }
            }
        }
        return false;
    }
}
