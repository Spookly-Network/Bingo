package de.zayon.bingo.listener;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
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
        if (!GameData.getIsHunger()) {
            e.setCancelled(true);
        } else {
            if (GameState.state != GameState.INGAME) {
                e.setCancelled(true);
            }
        }
    }
}
