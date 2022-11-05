package de.mxscha.minivaro.listeners.lobby;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.utils.item.ItemCreator;
import de.mxscha.minivaro.utils.location.ConfigLocationUtil;
import org.bukkit.*;
import org.bukkit.entity.Allay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class TeamRegisterListener implements Listener {

    private static final String TEAM_REGISTER_NAME = "§a§lTeam Registrieren";
    private static Inventory TEAM_REGISTER_INVENTORY;
    // new Team(teamName, shortcut, player1, player2).save();

    private static final HashMap<Player, Inventory> register = new HashMap<>();
    private static final HashMap<Player, Player> pending = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!MiniVaroCore.getInstance().getGameManager().isStarted()) {
            if (event.getRightClicked() instanceof Allay teamRegister) {
                if (teamRegister.getName().equals(TEAM_REGISTER_NAME)) {
                    event.setCancelled(true);
                    TeamManager manager = MiniVaroCore.getInstance().getTeamManager();
                    if (manager.hasATeam(player.getName())) {
                        if (event.getHand() == player.getHandRaised()) {
                            player.sendMessage(MiniVaroCore.getPrefix() + "§cDu bist bereits in einem Team!");
                        }
                    } else {
                        if (!register.containsKey(player)) {
                            TEAM_REGISTER_INVENTORY = Bukkit.createInventory(null, 9 * 5, TEAM_REGISTER_NAME);
                            player.openInventory(TEAM_REGISTER_INVENTORY);
                            fillWithGlass(TEAM_REGISTER_INVENTORY);
                            addExitButton(TEAM_REGISTER_INVENTORY);
                            addItems(player, TEAM_REGISTER_INVENTORY);
                        } else {
                            player.openInventory(register.get(player));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!MiniVaroCore.getInstance().getGameManager().isStarted()) {
            if (event.getView().getTitle().equals(TEAM_REGISTER_NAME)) {
                event.setCancelled(true);
                if (event.getCurrentItem() == null) return;
                if (!event.getCurrentItem().hasItemMeta()) return;
                switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
                    case "§8● §6Spieler 2 eintragen":
                        Inventory inventory = Bukkit.createInventory(null, InventoryType.ANVIL, "§a§lSpieler 2 eintragen");
                        inventory.setItem(0, new ItemCreator(Material.SKELETON_SKULL).setName("§6Spieler 2").toItemStack());
                        player.closeInventory();
                        player.openInventory(inventory);
                        break;
                }
            } else if (event.getView().getTitle().equals("§a§lSpieler 2 eintragen")) {
                InventoryView view = event.getView();
                int rawSlot = event.getRawSlot();
                if (rawSlot == view.convertSlot(rawSlot)) {
                    switch (rawSlot) {
                        case 2:
                            ItemStack item = view.getItem(rawSlot);
                            if (item == null) return;
                            try {
                                String name = item.getItemMeta().getDisplayName();
                                Player mate = Bukkit.getPlayer(ChatColor.stripColor(name));
                                player.closeInventory();
                                player.sendMessage(MiniVaroCore.getPrefix() + "§7Du hast §9" + mate.getName() + "§7 zu deinem Team eingeladen!");
                                mate.sendMessage(MiniVaroCore.getPrefix() + "§7Du wurdest von §9" + player.getName() + " §7zu einem Team eingeladen!");
                                mate.sendMessage(MiniVaroCore.getPrefix() + "§7Nehme diese Einladung mit §c/accept §8[§9pplayername§8] §7an!");
                                pending.put(player, mate);
                                TEAM_REGISTER_INVENTORY = Bukkit.createInventory(null, 9 * 5, TEAM_REGISTER_NAME);
                                player.openInventory(TEAM_REGISTER_INVENTORY);
                                fillWithGlass(TEAM_REGISTER_INVENTORY);
                                addExitButton(TEAM_REGISTER_INVENTORY);
                                if (!register.containsKey(player)) register.put(player, TEAM_REGISTER_INVENTORY);
                            } catch (Exception exception) {
                                player.sendMessage(MiniVaroCore.getPrefix() + "§cDieser Spieler ist nicht Online!");
                                player.closeInventory();
                            }
                            break;
                        case 0:
                            event.setCancelled(true);
                            break;
                    }
                }
            }
        }
    }

    private void addItems(Player player, Inventory inventory) {
        if (register.containsKey(player)) {
            if (pending.containsKey(player)) {
                inventory.setItem(13, new ItemCreator(Material.NAME_TAG).setName("§8● §aTeam Name eintragen").toItemStack());
                inventory.setItem(21, new ItemCreator(Material.PAPER).setName("§8● §9Team Kürzel eintragen").toItemStack());
                inventory.setItem(22, new ItemCreator(Material.WITHER_SKELETON_SKULL).setName("§8● §6Spieler 1 (Du)").toItemStack());
                inventory.setItem(23, new ItemCreator(Material.SKELETON_SKULL).setName("§8● §6" + pending.get(player.getName()).getName() + " §0(ausgehend)").toItemStack());
            }
        } else {
            inventory.setItem(13, new ItemCreator(Material.NAME_TAG).setName("§8● §aTeam Name eintragen").toItemStack());
            inventory.setItem(21, new ItemCreator(Material.PAPER).setName("§8● §9Team Kürzel eintragen").toItemStack());
            inventory.setItem(22, new ItemCreator(Material.WITHER_SKELETON_SKULL).setName("§8● §6Spieler 1 (Du)").toItemStack());
            inventory.setItem(23, new ItemCreator(Material.SKELETON_SKULL).setName("§8● §6Spieler 2 eintragen").toItemStack());
        }
    }


    public static void fillWithGlass(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemCreator(Material.GRAY_STAINED_GLASS_PANE).setName(" ").toItemStack());
        }
    }

    public static void addExitButton(Inventory inventory) {
        inventory.setItem(inventory.getSize() - 5, new ItemCreator(Material.REDSTONE).setName("§8● §cSchließen").toItemStack());
    }

    public void spawnTeamRegisterEntity() {
        despawnTeamRegisterEntity();
        Location lobbyTeamRegister = new ConfigLocationUtil("LobbyTeamRegister").loadLocation();
        if (lobbyTeamRegister == null) return;
        World world = lobbyTeamRegister.getWorld();
        Allay teamRegister = (Allay) world.spawnEntity(lobbyTeamRegister.add(0, 1, 0), EntityType.ALLAY);
        teamRegister.setAI(false);
        teamRegister.setInvulnerable(true);
        teamRegister.setSilent(true);
        teamRegister.setCustomName(TEAM_REGISTER_NAME);
        teamRegister.setCustomNameVisible(true);
    }

    public void despawnTeamRegisterEntity() {
        Bukkit.getWorlds().forEach(world -> {
            world.getEntities().forEach(entity -> {
                if (entity instanceof Allay allay) {
                    if (allay.getName().equals(TEAM_REGISTER_NAME)) {
                        entity.remove();
                    }
                }
            });
        });
    }
}
