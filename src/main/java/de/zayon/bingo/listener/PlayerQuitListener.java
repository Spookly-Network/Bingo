package de.zayon.bingo.listener;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.countdowns.EndingCoutdown;
import de.zayon.bingo.countdowns.LobbyCountdown;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.data.TeamData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerQuitListener implements Listener {

    private final Bingo bingo;

    public PlayerQuitListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void handleLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (GameState.state == GameState.LOBBY) {
            event.setQuitMessage(StringData.getPrefix() + StringData.getHighlightColor() + event.getPlayer().getName() + " §7hat das Spiel verlassen.");
            if(Bukkit.getOnlinePlayers().size() <= 2) {
                Bukkit.broadcastMessage(StringData.getPrefix() + "§7Der Countdown wurde gestopt weil zu wenig Spieler drin sind.");
                Bukkit.getScheduler().cancelTask(LobbyCountdown.scheduler);
            }
        } else if (GameState.state == GameState.INGAME && GameData.getIngame().contains(player)) {
            event.setQuitMessage(StringData.getPrefix() + StringData.getHighlightColor() + event.getPlayer().getName() + " §7hat das Spiel verlassen.");
            if (GameData.getIngame().size() <= 2) {
                EndingCoutdown.teamWin(TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(GameData.getIngame().get(0))));
            }
        } else {
            event.setQuitMessage("");
        }
    }
}
