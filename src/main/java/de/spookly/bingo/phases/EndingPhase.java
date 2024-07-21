package de.spookly.bingo.phases;

import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.GameData;
import de.spookly.bingo.data.GameState;
import de.spookly.bingo.data.StringData;
import de.spookly.bingo.data.helper.TextComponentHelper;
import de.spookly.bingo.statistics.player.BingoPlayerStats;
import de.spookly.bingo.util.AbstractGamePhase;
import de.spookly.bingo.util.Items;
import de.spookly.bingo.util.fonts.SmallPixelFont;
import de.spookly.bingo.util.fonts.TeamFont;
import de.spookly.bingo.util.playerheads.BigHeadMessage;
import de.spookly.bingo.util.playerheads.PlayerheadChatComponent;
import de.spookly.Spookly;
import de.spookly.player.SpooklyPlayer;
import de.spookly.team.Team;
import de.nehlen.spooklycloudnetutils.helper.CloudPlayerHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

public class EndingPhase extends AbstractGamePhase {

    private final SpooklyBingoPlugin spooklyBingoPlugin;

    public EndingPhase(SpooklyBingoPlugin spooklyBingoPlugin) {
        super(20);
        this.spooklyBingoPlugin = spooklyBingoPlugin;
    }

    public void teamWin(Team team) {

        GameState.state = GameState.END;
        spooklyBingoPlugin.getIngamePhase().endPhase();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != null) {
                if (GameData.getTeamSize() > 1)
                    player.showTitle(Title.title(team.prefix().font(TeamFont.KEY),
                            Component.text("hat das Spiel gewonnen").color(NamedTextColor.GRAY),
                            Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ZERO)));
                else
                    player.showTitle(Title.title(team.registeredPlayers().getFirst().displayName().color(StringData.getHighlightColor()),
                            Component.text("hat das Spiel gewonnen").color(NamedTextColor.GRAY),
                            Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ZERO)));
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 0);
                player.spawnParticle(Particle.TOTEM_OF_UNDYING, player.getLocation().add(0, 1, 0), 250);
            }
        }

        Bukkit.getScheduler().runTaskLater(SpooklyBingoPlugin.getSpooklyBingoPlugin(), () -> {
            AtomicReference<Component> winComponent = new AtomicReference<>(Component.empty()
                    .append(TextComponentHelper.newLineComponent())
                    .append(StringData.getPrefix()));

            for (Player player : Bukkit.getOnlinePlayers()) {
                SpooklyPlayer spooklyPlayer = Spookly.getPlayer(player);
                player.teleport(GameData.getLobbyLocation());
                player.getInventory().clear();
                player.setTotalExperience(0);
                player.setHealth(20L);
                player.setFoodLevel(20);
                player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA, Component.text("Zurück zur Lobby").color(NamedTextColor.GRAY), 1));
                spooklyPlayer.resetNameTag();
            }

            if (team.maxTeamSize() <= 1) {
                Player winner = team.registeredPlayers().getFirst();
                SpooklyPlayer spooklyPlayer = Spookly.getPlayer(winner.getUniqueId());
                BingoPlayerStats stats = spooklyBingoPlugin.getPlayerStatisticsManager().getPlayerStatistics(spooklyPlayer);

                winComponent.set(BigHeadMessage.getComponent(winner.getPlayer(), winner.displayName().color(StringData.getHighlightColor())
                        .append(Component.text(" hat das Spiel Gewonnen.").color(NamedTextColor.GRAY))));

                spooklyPlayer.addPoints(350);
                stats.setGamesWon(stats.getGamesWon() + 1);
                winner.sendMessage(TextComponentHelper.addPointsComponent(350));
            } else {
                winComponent.set(winComponent.get().append(team.prefix().font(TeamFont.KEY))
                        .append(Component.text(" hat das Spiel Gewonnen.").color(NamedTextColor.GRAY))
                        .append(TextComponentHelper.newLineComponent())
                        .append(StringData.getPrefix())
                        .append(Component.text("Zu diesem Team gehören:").color(NamedTextColor.GRAY)));

                team.registeredPlayers().forEach(player -> {
                    SpooklyPlayer spooklyPlayer = Spookly.getPlayer(player.getUniqueId());
                    BingoPlayerStats stats = spooklyBingoPlugin.getPlayerStatisticsManager().getPlayerStatistics(spooklyPlayer);
                    winComponent.set(winComponent.get().append(Component.newline())
                            .append(Component.text("- ").color(NamedTextColor.GRAY))
                            .append(PlayerheadChatComponent.getHeadComponent(player, SmallPixelFont.class).font(SmallPixelFont.KEY))
                            .append(Component.text(" "))
                            .append(player.displayName()));

                    spooklyPlayer.addPoints(250);
                    stats.setGamesWon(stats.getGamesWon() + 1);
                    player.sendMessage(TextComponentHelper.addPointsComponent(250));
                });
            }
            Bukkit.broadcast(winComponent.get().append(TextComponentHelper.newLineComponent()));
            startPhase();
        }, 100);
    }

    public void startPhase() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SpooklyBingoPlugin.getSpooklyBingoPlugin(), () -> {

            if (counter <= 10) {
                Bukkit.broadcast(StringData.getPrefix()
                        .append(Component.text("Der Server startet in ").color(NamedTextColor.GRAY))
                        .append(Component.text(counter + " Sekunden").color(StringData.getHighlightColor()))
                        .append(Component.text(" neu.").color(NamedTextColor.GRAY)));
            }
            if (counter == 0) {
                Bukkit.getScheduler().runTaskAsynchronously(SpooklyBingoPlugin.getSpooklyBingoPlugin(), () -> {
                    for (final Player player : Bukkit.getOnlinePlayers()) {
                        CloudPlayerHelper.sendPlayerToGroup(player, "Lobby", CloudPlayerHelper.SelectorType.RANDOM);
                    }
                });

            } else if (counter == -3) {
                Bukkit.getServer().shutdown();
            }
            counter--;
        }, 20, 20);
    }
}
