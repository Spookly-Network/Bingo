package de.zayon.bingo.listener;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetBridgePlugin;
import de.zayon.bingo.Bingo;
import de.zayon.bingo.countdowns.LobbyCountdown;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.data.TeamData;
import de.zayon.bingo.data.helper.Team;
import de.zayon.zayonapi.items.Items;
import io.sentry.Sentry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class PlayerInteractListener implements Listener {
    private final Bingo bingo;

    public PlayerInteractListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        try {
            Player player = event.getPlayer();
            if (GameState.state != GameState.INGAME) {
                event.setCancelled(true);
            }

            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§7Teamauswahl")) {
                    Inventory inv = Bukkit.createInventory(null, (int) Math.ceil(GameData.getTeamAmount() / 9) * 9, StringData.getHighlightColor() + "Team auswahl");

                    int i = 0;
                    for (Team t : TeamData.getTeamCache()) {
                        if (t.getMates().contains(player)) {
                            inv.setItem(i, Items.createLore(Material.LIME_DYE, StringData.getHighlightColor() + "Team-" + (i + 1), StringData.getHighlightColor() + t.getSize() + "§7/" + StringData.getHighlightColor() + t.getMaxSize(), 1));
                        } else if (t.getSize() == t.getMaxSize()) {
                            inv.setItem(i, Items.createLore(Material.RED_DYE, StringData.getHighlightColor() + "Team-" + (i + 1), StringData.getHighlightColor() + t.getSize() + "§7/" + StringData.getHighlightColor() + t.getMaxSize(), 1));
                        } else {
                            inv.setItem(i, Items.createLore(Material.LIGHT_GRAY_DYE, StringData.getHighlightColor() + "Team-" + (i + 1), StringData.getHighlightColor() + t.getSize() + "§7/" + StringData.getHighlightColor() + t.getMaxSize(), 1));
                        }
                        i++;
                    }

                    player.openInventory(inv);
                } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§7Zurück zur Lobby")) {
                    BridgePlayerManager.getInstance().proxySendPlayer(BridgePlayerManager.getInstance().getOnlinePlayer(player.getUniqueId()), "Lobby-1");
                } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§7Starte das Spiel")) {

                    if (Bukkit.getOnlinePlayers().size() >= 2) {
                        Bukkit.getScheduler().cancelTasks(Bingo.getBingo());
                        Bingo.getBingo().getLobbyCountdown().startLobbyCountdown(true);
                    } else {
                        player.sendMessage(StringData.getPrefix() + "Es sind leider " + StringData.getHighlightColor() + "nicht genug Spieler §7Im Spiel.");
                    }
                }
            }
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }
}
