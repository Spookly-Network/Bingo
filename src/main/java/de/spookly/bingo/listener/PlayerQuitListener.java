package de.spookly.bingo.listener;

import de.spookly.Spookly;
import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.GameData;
import de.spookly.bingo.data.GameState;
import de.spookly.player.PlayerUnregisterEvent;
import de.spookly.player.SpooklyPlayer;
import de.spookly.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.spookly.bingo.data.StringData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PlayerQuitListener implements Listener {

    private final SpooklyBingoPlugin spooklyBingoPlugin;

    public PlayerQuitListener(SpooklyBingoPlugin spooklyBingoPlugin) {
        this.spooklyBingoPlugin = spooklyBingoPlugin;
    }

    @EventHandler
    public void handleLeave(PlayerQuitEvent event) {
        try {
            Player player = event.getPlayer();
            Component quitMessage = StringData.getPrefix()
                    .append(Component.translatable("general.quit.message",
                            event.getPlayer().displayName().color(StringData.getHighlightColor())
                    ).color(NamedTextColor.GRAY));


            if (GameState.state == GameState.LOBBY) {
                event.quitMessage(quitMessage);
                if (Bukkit.getOnlinePlayers().size() <= GameData.getMinPlayerToStartGame()) {
                    Bukkit.broadcast(StringData.getPrefix()
                            .append(Component.translatable("phase.lobby.countdownStop").color(NamedTextColor.GRAY)));
                    SpooklyBingoPlugin.getSpooklyBingoPlugin().getLobbyPhase().counter(GameData.getStartTime());
                    SpooklyBingoPlugin.getSpooklyBingoPlugin().getLobbyPhase().endPhase();
                }

                if (GameData.getTeamCache().containsKey(player)) {
                    GameData.getTeamCache().get(player).removePlayer(player);
                }
            } else if (GameState.state == GameState.INGAME && GameData.getIngame().contains(player)) {
                event.quitMessage(quitMessage);

                Team team = GameData.getTeamCache().get(player);
                team.removePlayer(player);
                GameData.getTeamCache().remove(player);
                if (team.registeredPlayers().isEmpty()) {
                    Spookly.getTeamManager().removeTeam(team);
                }

                if (Spookly.getTeamManager().registeredTeams().size() == 1) {
                    SpooklyBingoPlugin.getSpooklyBingoPlugin().getEndingPhase().teamWin(Spookly.getTeamManager().registeredTeams().get(0));
                }
            } else {
                event.quitMessage(Component.empty());
            }
        } catch (Exception e) {
//            Sentry.captureException(e);
            e.printStackTrace();
        }
    }

    @EventHandler
    public void handleUnregister(PlayerUnregisterEvent event) {
        SpooklyPlayer player = event.getSpooklyPlayer();
        spooklyBingoPlugin.getPlayerStatisticsManager().savePlayerStatistics(player);
        player.save();
    }
}
