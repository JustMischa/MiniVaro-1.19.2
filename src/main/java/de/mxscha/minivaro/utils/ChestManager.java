package de.mxscha.minivaro.utils;

import com.sun.tools.javac.Main;
import de.mxscha.minivaro.utils.location.ConfigLocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ChestManager implements Listener {

    private static HashMap<Location, Inventory> invs = new HashMap<>();
    Location locationA = new ConfigLocationUtil("PortalArea1").loadLocation();
    Location locationB = new ConfigLocationUtil("PortalArea2").loadLocation();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.CHEST) {
                Chest chest = (Chest) event.getClickedBlock();
                RegionManager regionManager = new RegionManager();
                locationA = new ConfigLocationUtil("PortalArea1").loadLocation();
                locationB = new ConfigLocationUtil("PortalArea2").loadLocation();
                if (locationA == null || locationB == null) return;
                if (regionManager.isInRegion(event.getClickedBlock().getLocation(), locationA, locationB)) {


                   if (!invs.containsKey(event.getClickedBlock().getLocation())) {
                        event.setCancelled(true);
                        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST);
                        player.openInventory(inventory);
                        int n = new Random().nextInt(10);
                        List<ItemStack> items = new ArrayList<ItemStack>();
                        addItemsToList(items);
                        while (n != 0) {
                            n--;
                            Random rdm1 = new Random();
                            Random rdm2 = new Random();
                            int n3 = rdm2.nextInt(27);
                            int n4 = rdm1.nextInt(items.size());
                            inventory.setItem(n3, items.get(n4));
                            //items.remove(items.get(n4));
                        }
                        invs.put(event.getClickedBlock().getLocation(), inventory);
                    } else {
                        player.openInventory(invs.get(event.getClickedBlock().getLocation()));
                    }
                }
            }
        }
    }

    private void addItemsToList(List<ItemStack> items) {
        items.add(new ItemStack(Material.WOODEN_AXE));
        items.add(new ItemStack(Material.OAK_PLANKS, new Random().nextInt(5)));
        items.add(new ItemStack(Material.STRING, new Random().nextInt(7)));
        items.add(new ItemStack(Material.APPLE, new Random().nextInt(3)));
    }
}