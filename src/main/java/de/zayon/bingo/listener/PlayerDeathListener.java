package de.zayon.bingo.listener;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.data.TeamData;
import org.bukkit.Bukkit;
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
        event.setRespawnLocation(TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(player)).getSpawnLoc());
    }
}
