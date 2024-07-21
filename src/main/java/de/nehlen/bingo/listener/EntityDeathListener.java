package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.data.helper.TextComponentHelper;
import de.nehlen.bingo.statistics.player.BingoPlayerStats;
import de.spookly.Spookly;
import de.spookly.player.SpooklyPlayer;
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

import java.util.Optional;

public class EntityDeathListener implements Listener {

    private final Bingo bingo;

    public EntityDeathListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void handleDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        Optional<Player> killer = Optional.ofNullable(event.getEntity().getKiller());
        if (killer.isPresent()) {
            Player player = killer.get();
            SpooklyPlayer spooklyPlayer = Spookly.getPlayer(player);
            BingoPlayerStats stats = bingo.getPlayerStatisticsManager().getPlayerStatistics(spooklyPlayer);

            if (event.getEntityType().equals(EntityType.SHEEP)) {
                Sheep sheep = (Sheep) entity;
                if (sheep.getColor().equals(DyeColor.BROWN)) {
                    player.sendMessage(StringData.getPrefix()
                            .append(Component.translatable("stats.brownSheep.add",
                                    Component.translatable("stats.brownSheep.sheep").color(StringData.getHighlightColor())
                            ).color(NamedTextColor.GRAY)));
                    player.sendMessage(TextComponentHelper.addPointsComponent(20));
                    stats.setBrownSheep(stats.getBrownSheep() + 1);
                    spooklyPlayer.addPoints(20);
                }
            }
        }
        return;
    }
}
