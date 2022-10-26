package de.mxscha.minivaro.commands;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.utils.timer.GameStartCountdown;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminStartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("varo.start")) {
                switch (args.length) {
                    case 0:
                        MiniVaroCore.getInstance().getCountdown().start();
                        break;
                    case 1:
                        if (args[0].equalsIgnoreCase("cancel")) {
                            MiniVaroCore.getInstance().getCountdown().stop();
                            player.sendMessage(MiniVaroCore.getPrefix() + "§cDu hast den Countdown abgebrochen!");
                            for (Player online : Bukkit.getOnlinePlayers()) {
                                online.sendTitle("§c§lACHTUNG", "§9Der Start wurde abgebrochen!");
                            }
                        }
                        break;
                }
            }
        }
        return false;
    }
}
