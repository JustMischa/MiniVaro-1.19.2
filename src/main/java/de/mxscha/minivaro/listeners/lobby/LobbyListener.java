package de.mxscha.minivaro.listeners.lobby;

import com.google.common.base.Predicates;
import de.mxscha.minivaro.MiniVaroCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class LobbyListener implements Listener {



    @EventHandler
    public void onLobbyBlockPlace(BlockPlaceEvent event) {
        if (!MiniVaroCore.getInstance().getGameManager().isStarted()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyBlockDamage(BlockBreakEvent event) {
        if (!MiniVaroCore.getInstance().getGameManager().isStarted()) {
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!MiniVaroCore.getInstance().getGameManager().isStarted()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLobbyDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!MiniVaroCore.getInstance().getGameManager().isStarted()) {
                event.setCancelled(true);
            }
        }
    }
}
