package de.nehlen.bingo.listener;

import de.nehlen.bingo.data.GameData;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.player.SpooklyPlayer;
import de.nehlen.spookly.team.PlayerJoinTeamEvent;
import de.nehlen.spookly.team.PlayerQuitTeamEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TeamListener implements Listener {

    public TeamListener() {
        Spookly.getServer().getEventExecuter().register(PlayerJoinTeamEvent.class, event -> {
            SpooklyPlayer player = event.getSpooklyPlayer();
            GameData.getTeamCache().put(event.getSpooklyPlayer().toPlayer(), event.getTeam());
            if(GameData.getTeamSize() > 1)
                player.prefix(event.getTeam().prefix(), (event.getTeam().tabSortId()+20));
        });

        Spookly.getServer().getEventExecuter().register(PlayerQuitTeamEvent.class, event -> {
            SpooklyPlayer player = event.getSpooklyPlayer();
            GameData.getTeamCache().remove(event.getSpooklyPlayer().toPlayer());
            player.resetNameTag();
        });
    }
}
