package de.spookly.bingo.listener;

import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.GameData;
import de.spookly.bingo.data.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private final SpooklyBingoPlugin spooklyBingoPlugin;

    public DamageListener(SpooklyBingoPlugin spooklyBingoPlugin) {
        this.spooklyBingoPlugin = spooklyBingoPlugin;
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
