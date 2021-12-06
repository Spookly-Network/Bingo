package de.nehlen.bingo.listener;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.executor.ServerSelectorType;
import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.inventroy.TeamSelectInventroy;
import io.sentry.Sentry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class PlayerInteractListener implements Listener {
    private final Bingo bingo;
    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

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
                    player.openInventory((Inventory) new TeamSelectInventroy(player).build());
                } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§7Zurück zur Lobby")) {
                    playerManager.getPlayerExecutor(playerManager.getOnlinePlayer(player.getUniqueId())).connectToGroup("Lobby", ServerSelectorType.RANDOM);
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
