package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private final Bingo bingo;

    public DamageListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {

        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(GameState.state != GameState.INGAME) {
                e.setCancelled(true);
            }
            if(!GameData.getIngame().contains(p)) {
                e.setCancelled(true);
            }
        } else {
            return;
        }
    }
}
