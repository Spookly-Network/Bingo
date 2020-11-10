package de.zayon.bingo.listener;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
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

        if(GameState.state != GameState.INGAME || GameData.getIngame().contains(player)) {
            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + event.getPlayer().getName() + " §7hat das Spiel verlassen.");

            if(GameData.getIngame().size() < 1) {
                //TODO Last one winning
            }
        }
        if(GameState.state == GameState.LOBBY && (Bukkit.getOnlinePlayers().size() <= 1)) {
            Bukkit.broadcastMessage(StringData.getPrefix() + "§7Der Countdown wurde gestopt weil zu wenig Spieler drin sind.");
            Bukkit.getScheduler().cancelTasks(Bingo.getBingo());
        }
    }
}
