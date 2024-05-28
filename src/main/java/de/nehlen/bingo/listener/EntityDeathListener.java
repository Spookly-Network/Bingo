package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.data.helper.TextComponentHelper;
import de.nehlen.bingo.factory.UserFactory;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.player.SpooklyPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            SpooklyPlayer spooklyPlayer = Spookly.getPlayer(player);

            if (event.getEntityType().equals(EntityType.SHEEP)) {
                Sheep sheep = (Sheep) entity;
                if (sheep.getColor().equals(DyeColor.BROWN)) {
                    player.sendMessage(StringData.getPrefix()
                            .append(Component.translatable("stats.brownSheep.add",
                                    Component.translatable("stats.brownSheep.sheep").color(StringData.getHighlightColor())
                            ).color(NamedTextColor.GRAY)));
                    player.sendMessage(TextComponentHelper.addPointsComponent(20));
                    spooklyPlayer.addPoints(20);
                    this.bingo.getUserFactory().updateBrownSheeps(player, UserFactory.UpdateType.ADD, 1);
                }
            }
        }
        return;
    }
}
