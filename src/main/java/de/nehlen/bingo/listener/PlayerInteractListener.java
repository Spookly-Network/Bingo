package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.inventroy.BingoListInventory;
import de.nehlen.bingo.inventroy.SpectatorInventory;
import de.nehlen.bingo.inventroy.TeamSelectInventory;
import de.nehlen.bingo.util.UtilFunctions;
import de.nehlen.spooklycloudnetutils.helper.CloudPlayerHelper;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {
    private final Bingo bingo;

    public PlayerInteractListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        try {
            Player player = event.getPlayer();
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (GameState.state == GameState.LOBBY || GameState.state == GameState.END) {
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    switch (itemStack.getType()) {
                        case MAP: {
                            new BingoListInventory(player).open();
                            player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
                            event.setCancelled(true);
                            break;
                        }
                        case TOTEM_OF_UNDYING: {
                            new TeamSelectInventory(UtilFunctions.getTeamInventorySize(), player).open();
                            break;
                        }
                        case HEART_OF_THE_SEA: {
                            CloudPlayerHelper.sendPlayerToGroup(player, "Lobby", CloudPlayerHelper.SelectorType.RANDOM);
                            break;
                        }
                    }
                } else {
                    if (player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                        if (GameData.getIngame().contains(player)) return;
                        new SpectatorInventory(player);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
