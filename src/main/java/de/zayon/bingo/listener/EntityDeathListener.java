package de.zayon.bingo.listener;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.factory.UserFactory;
import de.zayon.zayonapi.PointsAPI.PointsAPI;
import de.zayon.zayonapi.ZayonAPI;
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
                    ZayonAPI.getZayonAPI().getPointsAPI().updatePoints(player, PointsAPI.UpdateType.ADD, 100);
                    this.bingo.getUserFactory().updateBrownSheeps(player, UserFactory.UpdateType.ADD, 1);
                }
            }
        }
        return;
    }
}
