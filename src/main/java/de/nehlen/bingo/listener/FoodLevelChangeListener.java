package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
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
        if(GameData.getIsHunger()) return;
        e.setCancelled(true);
    }
}
