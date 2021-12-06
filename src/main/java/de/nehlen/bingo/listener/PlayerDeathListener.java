package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.StringData;
import de.nehlen.gameapi.TeamAPI.Team;
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
        event.setDeathMessage(StringData.getPrefix() + StringData.getHighlightColor() + player.getName() + " §7ist gestorben.");

    }

    @EventHandler
    public void handleRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Team team = GameData.getTeamCache().get(player);
        event.setRespawnLocation((Location) team.getMemory().get("spawnLoc"));
    }
}
