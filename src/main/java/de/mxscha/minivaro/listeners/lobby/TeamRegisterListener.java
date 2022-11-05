package de.mxscha.minivaro.listeners.lobby;

import de.mxscha.minivaro.MiniVaroCore;
import de.mxscha.minivaro.database.teams.Team;
import de.mxscha.minivaro.database.teams.TeamManager;
import de.mxscha.minivaro.utils.item.ItemCreator;
import de.mxscha.minivaro.utils.location.ConfigLocationUtil;
import de.mxscha.minivaro.utils.scoreboard.LobbyScoreboard;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Allay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class TeamRegisterListener implements Listener {

    private static final String TEAM_REGISTER_NAME = "§a§lTeam-verwaltung";

    private static final HashMap<Player, Inventory> register = new HashMap<>();
    private static final HashMap<Player, Player> pending = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!MiniVaroCore.getInstance().getGameManager().isStarted()) {
            if (event.getRightClicked() instanceof Allay teamRegister) {
                if (teamRegister.getName().equals(TEAM_REGISTER_NAME)) {
                    event.setCancelled(true);
                    Inventory TEAM_REGISTER_INVENTORY = Bukkit.createInventory(null, 9 * 5, TEAM_REGISTER_NAME);
                    player.openInventory(TEAM_REGISTER_INVENTORY);
                    fillWithGlass(TEAM_REGISTER_INVENTORY);
                    addExitButton(TEAM_REGISTER_INVENTORY);
                    addItemsToMenu(TEAM_REGISTER_INVENTORY);
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
                TeamManager manager = MiniVaroCore.getInstance().getTeamManager();
                switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
                    case "§8● §9Team bearbeiten":
                        if (manager.hasATeam(player.getName())) {
                            if (manager.isPlayer1(player.getName())) {
                                Inventory EditInventory = Bukkit.createInventory(null, 9*6, "§8● §9Team bearbeiten");
                                fillWithGlass(EditInventory);
                                addBackButton(EditInventory);
                                addItemsToEdit(EditInventory);
                                player.openInventory(EditInventory);
                            } else
                                player.sendMessage(MiniVaroCore.getPrefix() + "§cNur der Teamführer darf das Team bearbeiten!");
                        } else
                            player.sendMessage(MiniVaroCore.getPrefix() + "§cDu musst in einem Team sein um es zu bearbeiten!");
                        break;
                    case "§8● §9Team registrieren":
                        if (!manager.hasATeam(player.getName())) {
                            Inventory RegisterInventory = Bukkit.createInventory(null, 9 * 6, "§8● §9Spieler 2 finden");
                            Bukkit.getOnlinePlayers().forEach(online -> {
                                if (online != player) {
                                    if (!manager.hasATeam(online.getName())) {
                                        RegisterInventory.addItem(new ItemCreator(Material.PLAYER_HEAD).setSkull("§8● §9" + online.getName(),
                                                online, "§8● §7klicke zum diesen Spieler einzuladen!").toItemStack());
                                    }
                                }
                            });

                            int rest = 9*6-Bukkit.getOnlinePlayers().size();
                            for (int i = rest; i >= 0; i--) {
                                RegisterInventory.setItem(i, new ItemCreator(Material.GRAY_STAINED_GLASS_PANE).setName(" ").toItemStack());
                            }
                            RegisterInventory.setItem(9 * 6-1, new ItemCreator(Material.BARRIER).setName("§8● §cAlleine Spielen").toItemStack());
                            addBackButton(RegisterInventory);
                            player.closeInventory();
                            player.openInventory(RegisterInventory);
                        } else
                            player.sendMessage(MiniVaroCore.getPrefix() + "§cDu bist bereits in einem Team");
                        break;
                    case "§8● §cSchließen":
                        player.closeInventory();
                        break;
                }
            } else if (event.getView().getTitle().equals("§8● §9Spieler 2 finden")) {
                event.setCancelled(true);
                if (event.getCurrentItem().getType() == Material.PLAYER_HEAD){
                    if (event.getCurrentItem() == null) return;
                    String object = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                    String invitePlayer = object.substring(2);
                    Player invite = Bukkit.getPlayer(invitePlayer);
                    if (invite.isOnline()) {
                        if (pending.containsKey(player)) {
                            if (pending.get(player).equals(invite)) {
                                player.sendMessage(MiniVaroCore.getPrefix() + "§cDu hast diesen Spieler bereits eingeladen!");
                            }
                        } else {
                            TextComponent prefix = new TextComponent(MiniVaroCore.getPrefix());
                            TextComponent accept = new TextComponent("§a§lANNEHMEN");
                            TextComponent middle = new TextComponent(" §8| ");
                            TextComponent decline = new TextComponent("§c§lABLEHNEN");
                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + player.getName()));
                            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a/accept §9"+player.getName()).create()));

                            decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/decline " + player.getName()));
                            decline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§c0/decline §9"+player.getName()).create()));

                            player.sendMessage(MiniVaroCore.getPrefix() + "§7Du hast §9" + invite.getName() + "§7 zu deinem Team eingeladen!");
                            invite.sendMessage(MiniVaroCore.getPrefix() + "§7Du wurdest von §9" + player.getName() + "§7 zu seinem Team eingeladen!");
                            invite.sendMessage(prefix, accept, middle, decline);
                            pending.put(player, invite);
                        }
                    } else {
                        player.sendMessage(MiniVaroCore.getPrefix() + "§cDieser Spieler ist nicht Online!");
                    }
                } else {
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§8● §cAlleine Spielen")) {
                        player.closeInventory();
                        TeamManager manager = MiniVaroCore.getInstance().getTeamManager();
                        int teamID = manager.getLastTeamsID();
                        new Team("Team"+teamID, "#"+teamID, player, null).save();
                        player.sendMessage(MiniVaroCore.getPrefix() + "§7Du bist nun im Team §8[§a"+manager.getTeam(player.getName())+"§8]");
                        Bukkit.getOnlinePlayers().forEach(LobbyScoreboard::new);
                    }
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§8● §cZurück")) {
                        Inventory TEAM_REGISTER_INVENTORY = Bukkit.createInventory(null, 9 * 5, TEAM_REGISTER_NAME);
                        player.openInventory(TEAM_REGISTER_INVENTORY);
                        fillWithGlass(TEAM_REGISTER_INVENTORY);
                        addExitButton(TEAM_REGISTER_INVENTORY);
                        addItemsToMenu(TEAM_REGISTER_INVENTORY);
                    }
                }
            }
        }
    }

    private void addItemsToEdit(Inventory inventory) {
        inventory.setItem(20, new ItemCreator(Material.NAME_TAG).setName("§8● §9Team Name").toItemStack());
        inventory.setItem(22, new ItemCreator(Material.MAP).setName("§8● §9Team Kürzel").toItemStack());
        inventory.setItem(24, new ItemCreator(Material.GOLDEN_HELMET).setName("§8● §cTeamführer übertragen").setLore("§c§lACHTUNG§7,", "§7Du bist dann nicht mehr der Teamführer!").toItemStack());

    }

    private void addItemsToMenu(Inventory inventory) {
        inventory.setItem(21, new ItemCreator(Material.PAPER).setName("§8● §9Team bearbeiten").toItemStack());
        inventory.setItem(23, new ItemCreator(Material.SPYGLASS).setName("§8● §9Team registrieren").toItemStack());
    }

    public static void fillWithGlass(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemCreator(Material.GRAY_STAINED_GLASS_PANE).setName(" ").toItemStack());
        }
    }

    public static void addBackButton(Inventory inventory) {
        inventory.setItem(inventory.getSize() - 9, new ItemCreator(Material.REDSTONE).setName("§8● §cZurück").toItemStack());
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

    public static HashMap<Player, Player> getPending() {
        return pending;
    }
}
