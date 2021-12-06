package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {
    private final Bingo bingo;

    public FoodLevelChangeListener(Bingo bingo) {
        this.bingo = bingo;
    }
    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }
}
