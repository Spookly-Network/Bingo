package de.zayon.bingo.listener;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import io.sentry.Sentry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    private final Bingo bingo;

    public AsyncPlayerChatListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();

        e.setCancelled(true);

        for(Player players: Bukkit.getOnlinePlayers()) {
            String teamPrefix = "";
            if(GameData.getTeamCache().containsKey(players)) {
                teamPrefix = "§7[" + GameData.getTeamCache().get(p).getTeamName() + "§7] ";
            }

            if(GameState.state != GameState.END) {
                if(!GameData.getIngame().contains(p)) {
                    if(!GameData.getIngame().contains(players))	{
                        players.sendMessage("§4✘§r " + p.getDisplayName() + "§7: §r" + e.getMessage());
                    }
                } else {
                    players.sendMessage(p.getDisplayName() + "§7: §r" + e.getMessage());
                }
            } else {
                players.sendMessage(p.getDisplayName() + "§7: §r" + e.getMessage());
            }
        }
    }
}

