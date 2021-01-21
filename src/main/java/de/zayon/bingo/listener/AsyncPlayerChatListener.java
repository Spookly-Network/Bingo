package de.zayon.bingo.listener;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.data.TeamData;
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
    public void onChat(AsyncPlayerChatEvent event) {
        try {
            Player p = event.getPlayer();

            event.setCancelled(true);

            for (Player players : Bukkit.getOnlinePlayers()) {
                String teamPrefix = "";
                if (TeamData.getPlayerTeamCache().containsKey(players)) {
                    teamPrefix = "§7[" + StringData.getHighlightColor() + "T-" + (TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(p)).getTeamID() + 1) + "§7] ";
                }

                if (GameState.state != GameState.END) {
                    if (!GameData.getIngame().contains(p)) {
                        if (!GameData.getIngame().contains(players)) {
                            players.sendMessage("§4✘§r " + p.getDisplayName() + "§7: §r" + event.getMessage());
                        }
                    } else {
                        players.sendMessage(teamPrefix + p.getDisplayName() + "§7: §r" + event.getMessage());
                    }
                } else {
                    players.sendMessage(teamPrefix + p.getDisplayName() + "§7: §r" + event.getMessage());
                }
            }
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }
}

