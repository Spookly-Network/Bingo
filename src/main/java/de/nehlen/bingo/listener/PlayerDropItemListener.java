package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

    private final Bingo bingo;

    public PlayerDropItemListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void handleItemDrop(PlayerDropItemEvent event) {

        Player player = event.getPlayer();
        if(!GameData.getIngame().contains(player) || GameState.state == GameState.END) {
            event.setCancelled(true);
        }
    }
}
