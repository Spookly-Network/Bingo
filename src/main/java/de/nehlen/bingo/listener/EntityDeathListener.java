package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.factory.UserFactory;
import de.nehlen.gameapi.Gameapi;
import de.nehlen.gameapi.PointsAPI.PointsAPI;
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
        Entity killer = event.getEntity().getKiller();
        if (killer instanceof Player) {
            Player player = (Player) killer;
            if (event.getEntityType().equals(EntityType.SHEEP)) {
                Sheep sheep = (Sheep) entity;
                if (sheep.getColor().equals(DyeColor.BROWN)) {
                    player.sendMessage(StringData.getPrefix() + "Du hast ein " + StringData.getHighlightColor() + "Braunes-Schaf §7getötet!");
                    player.sendMessage(StringData.getPrefix() + "Du hast §c100 §7Punkte erhalten.");
                    Gameapi.getGameapi().getPointsAPI().updatePoints(player, PointsAPI.UpdateType.ADD, 100);
                    this.bingo.getUserFactory().updateBrownSheeps(player, UserFactory.UpdateType.ADD, 1);
                }
            }
        }
        return;
    }
}
