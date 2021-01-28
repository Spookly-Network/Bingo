package de.zayon.bingo.listener;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.factory.UserFactory;
import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

    private final Bingo bingo;

    public EntityDeathListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void handleDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity.getLastDamageCause() instanceof Player) {
            if (event.getEntityType().equals(EntityType.SHEEP)) {
                Sheep sheep = (Sheep) entity;
                Player player = (Player) entity.getLastDamageCause();
                if (sheep.getColor().equals(DyeColor.BROWN)) {
                    this.bingo.getUserFactory().updateBrownSheeps(player, UserFactory.UpdateType.ADD, 1);
                }
            }
        }
        return;
    }
}
