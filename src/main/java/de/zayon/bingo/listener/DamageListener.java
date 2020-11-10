package de.zayon.bingo.listener;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
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
