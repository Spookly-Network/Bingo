package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
import de.nehlen.gameapi.Gameapi;
import de.nehlen.gameapi.TeamAPI.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final Bingo bingo;

    public PlayerQuitListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void handleLeave(PlayerQuitEvent event) {
        try {
            Player player = event.getPlayer();
            Component quitMessage = StringData.getPrefix()
                    .append(event.getPlayer().name().color(StringData.getHighlightColor()))
                    .append(Component.text(" hat das Spiel verlassen.").color(NamedTextColor.GRAY));

            if (GameState.state == GameState.LOBBY) {
                event.quitMessage(quitMessage);
                if (Bukkit.getOnlinePlayers().size() <= GameData.getMinPlayerToStartGame()) {
                    Bukkit.broadcast(StringData.getPrefix()
                            .append(Component.text("Der Countdown wurde gestopt weil zu wenig Spieler drin sind.").color(NamedTextColor.GRAY)));
                    Bingo.getBingo().getLobbyCountdown().endPhase();
                }
            } else if (GameState.state == GameState.INGAME && GameData.getIngame().contains(player)) {
                event.quitMessage(quitMessage);

                Team team = GameData.getTeamCache().get(player);
                team.removePlayer(player);
                GameData.getTeamCache().remove(player);
                if(team.registeredPlayers().isEmpty()) {
                    Gameapi.getGameapi().getTeamAPI().removeTeam(team);
                }

                if (Gameapi.getGameapi().getTeamAPI().registeredTeams().size() == 1) {
                    Bingo.getBingo().getEndingCoutdown().teamWin(GameData.getTeamCache().get(GameData.getIngame().get(0)));
                }
            } else {
                event.quitMessage(Component.empty());
            }

            if(GameData.getTeamCache().containsKey(player)) {
                GameData.getTeamCache().get(player).removePlayer(player);
            }
        } catch (Exception e) {
//            Sentry.captureException(e);
            e.printStackTrace();
        }
    }
}
