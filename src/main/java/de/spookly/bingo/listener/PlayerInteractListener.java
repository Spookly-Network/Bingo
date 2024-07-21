package de.spookly.bingo.listener;

import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.GameData;
import de.spookly.bingo.data.GameState;
import de.spookly.bingo.inventroy.SpectatorInventory;
import de.spookly.bingo.inventroy.TeamSelectInventory;
import de.spookly.bingo.util.UtilFunctions;
import de.spookly.bingo.inventroy.BingoListInventory;

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
    private final SpooklyBingoPlugin spooklyBingoPlugin;

    public PlayerInteractListener(SpooklyBingoPlugin spooklyBingoPlugin) {
        this.spooklyBingoPlugin = spooklyBingoPlugin;
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
