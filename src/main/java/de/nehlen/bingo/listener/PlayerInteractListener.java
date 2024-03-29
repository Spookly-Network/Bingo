package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.inventroy.BingoListInventory;
import de.nehlen.spooklycloudnetutils.SenderUtil;
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
            if (GameState.state != GameState.INGAME) {
                if (event.getMaterial().equals(Material.WRITTEN_BOOK)) return;
                event.setCancelled(true);
            }

            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (GameState.state == GameState.LOBBY || GameState.state == GameState.END) {
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    switch (itemStack.getType()) {
                        case CHEST: {
                            new BingoListInventory(player).open();
                            player.playSound(player, Sound.BLOCK_CHEST_OPEN, 1, 1);
                            break;
                        }
                        case TOTEM_OF_UNDYING: {
                            player.openInventory(bingo.getTeamSelectInventroy().getInventory(player));
                            break;
                        }
                        case HEART_OF_THE_SEA: {
                            SenderUtil.sendPlayerToGroup(player, "Lobby");
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
