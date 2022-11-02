package de.mxscha.minivaro.listeners.lobby;

import com.google.common.base.Predicates;
import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.commands.AdminCommand;
import de.mxscha.minivaro.utils.location.ConfigLocationUtil;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class LobbyListener implements Listener {



    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!MiniVaroCore.getInstance().getGameManager().isStarted()) {
            if (!AdminCommand.getAdmin().contains(event.getPlayer())) {
                Location spawn = new ConfigLocationUtil("Lobby").loadLocation();
                if (spawn == null) return;
                if (player.getLocation().getY() <= spawn.getY()-80) {
                    player.teleport(spawn);
                }
            }
        }
    }

    @EventHandler
    public void onLobbyBlockPlace(BlockPlaceEvent event) {
        if (!MiniVaroCore.getInstance().getGameManager().isStarted()) {
            if (!AdminCommand.getAdmin().contains(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLobbyBlockDamage(BlockBreakEvent event) {
        if (!MiniVaroCore.getInstance().getGameManager().isStarted()) {
            if (!AdminCommand.getAdmin().contains(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLobbyHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!MiniVaroCore.getInstance().getGameManager().isStarted()) {
                if (!AdminCommand.getAdmin().contains(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof ArmorStand) {
            if (!MiniVaroCore.getInstance().getGameManager().isStarted()) {
                if (!AdminCommand.getAdmin().contains(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onLobbyDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            if (!MiniVaroCore.getInstance().getGameManager().isStarted()) {
                if (!AdminCommand.getAdmin().contains(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onLobbyDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!MiniVaroCore.getInstance().getGameManager().isStarted()) {
                if (!AdminCommand.getAdmin().contains(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
