package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPingListener implements Listener {

    private final Bingo bingo;

    public ServerPingListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void handleServerPing(ServerListPingEvent event) {
        event.setMotd(GameData.getTeamAmount() + "x" + GameData.getTeamSize());

        if (GameState.state == GameState.LOBBY) {
            event.setMaxPlayers(GameData.getTeamAmount() * GameData.getTeamSize());
        } else {
            event.setMaxPlayers((GameData.getTeamAmount() * GameData.getTeamSize()) + 20);
        }
    }
}
