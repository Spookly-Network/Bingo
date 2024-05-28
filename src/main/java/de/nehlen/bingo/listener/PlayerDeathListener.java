package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.factory.UserFactory;
import de.nehlen.spookly.team.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathListener implements Listener {
    private final Bingo bingo;

    public PlayerDeathListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void handleDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        bingo.getUserFactory().updateDeaths(player, UserFactory.UpdateType.ADD, 1);
        event.deathMessage(StringData.getPrefix().append(player.name().color(StringData.getHighlightColor()))
                .append(Component.text(" ist gestorben.").color(NamedTextColor.GRAY)));

    }

    @EventHandler
    public void handleRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Team team = GameData.getTeamCache().get(player);
        if(player.getBedSpawnLocation() == null) {
            event.setRespawnLocation((Location) team.memory().get("spawnLoc"));
        }
    }
}
