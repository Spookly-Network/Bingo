package de.nehlen.bingo.listener;

import de.nehlen.bingo.data.GameData;
import de.spookly.Spookly;
import de.spookly.player.SpooklyPlayer;
import de.spookly.team.PlayerJoinTeamEvent;
import de.spookly.team.PlayerQuitTeamEvent;
import org.bukkit.event.Listener;

public class TeamListener implements Listener {

    public TeamListener() {
        Spookly.getServer().getEventExecuter().register(PlayerJoinTeamEvent.class, event -> {
            SpooklyPlayer player = event.getSpooklyPlayer();
            GameData.getTeamCache().put(event.getSpooklyPlayer().toPlayer(), event.getTeam());
            if(GameData.getTeamSize() > 1) {
                player.nameColor(event.getTeam().getTeamDisplay().getColor());
                player.prefix(event.getTeam().getTeamDisplay().getPrefix(), (event.getTeam().tabSortId() + 20));
            }
        });

        Spookly.getServer().getEventExecuter().register(PlayerQuitTeamEvent.class, event -> {
            SpooklyPlayer player = event.getSpooklyPlayer();
            GameData.getTeamCache().remove(event.getSpooklyPlayer().toPlayer());
            player.resetNameTag();
        });
    }
}
