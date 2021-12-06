package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildListener implements Listener {

    private final Bingo bingo;

    public BuildListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void handleBuild(BlockPlaceEvent event) {

        if(GameState.state != GameState.INGAME) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void handleBuild(BlockBreakEvent event) {

        if(GameState.state != GameState.INGAME) {
            event.setCancelled(true);
        }

    }
}
