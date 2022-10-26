package de.mxscha.minivaro.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class ProtectionTimeListener implements Listener {

    private static final ArrayList<Player> protection = new ArrayList<>();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getDamager() instanceof Player damager) {
                if (protection.contains(player)) {
                    event.setCancelled(true);
                }
                if (protection.contains(damager)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public static ArrayList<Player> getProtected() {
        return protection;
    }
}
