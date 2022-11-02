package de.mxscha.minivaro.commands;

import de.mxscha.minivaro.MiniVaroCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdminCommand implements CommandExecutor {

    private static final ArrayList<Player> admin = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("varo.admin")) {
                if (admin.contains(player)) {
                    admin.remove(player);
                    player.sendMessage(MiniVaroCore.getPrefix() + "§9Du bist nicht mehr im §cadmin mode");
                } else {
                    admin.add(player);
                    player.sendMessage(MiniVaroCore.getPrefix() + "§9Du bist nun im §cadmin mode");
                }
            }
        }
        return false;
    }

    public static ArrayList<Player> getAdmin() {
        return admin;
    }
}
