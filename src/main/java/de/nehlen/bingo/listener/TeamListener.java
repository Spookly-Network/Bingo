package de.nehlen.bingo.listener;

import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.StringData;
import de.nehlen.gameapi.TeamAPI.PlayerJoinTeamEvent;
import de.nehlen.gameapi.TeamAPI.PlayerQuitTeamEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TeamListener implements Listener {

    @EventHandler
    public void handleTeamJoin(PlayerJoinTeamEvent event) {
        Player player = event.getPlayer();
        event.getPlayer().playerListName(StringData.playerListName(player));
        GameData.getTeamCache().put(event.getPlayer(), event.getTeam());
    }

    @EventHandler
    public void handleTeamQuit(PlayerQuitTeamEvent event) {
        Player player = event.getPlayer();
        event.getPlayer().playerListName(StringData.playerListName(player));
        GameData.getTeamCache().remove(event.getPlayer());
    }
}
