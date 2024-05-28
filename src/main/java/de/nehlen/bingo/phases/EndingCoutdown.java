package de.nehlen.bingo.phases;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.util.fonts.SmallPixelFont;
import de.nehlen.bingo.util.fonts.TeamFont;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.data.helper.TextComponentHelper;
import de.nehlen.bingo.factory.UserFactory;
import de.nehlen.bingo.util.AbstractGamePhase;
import de.nehlen.bingo.util.Items;
import de.nehlen.bingo.util.playerheads.BigHeadMessage;
import de.nehlen.bingo.util.playerheads.PlayerheadChatComponent;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.player.SpooklyPlayer;
import de.nehlen.spookly.team.Team;
import de.nehlen.spooklycloudnetutils.SenderUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

public class EndingCoutdown extends AbstractGamePhase {

    private final Bingo bingo;

    public EndingCoutdown(Bingo bingo) {
        super(20);
        this.bingo = bingo;
    }

    public void teamWin(Team team) {

        GameState.state = GameState.END;
        bingo.getIngameCountdown().endPhase();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != null) {
                if (GameData.getTeamSize() > 1)
                    player.showTitle(Title.title(team.prefix().font(TeamFont.KEY),
                            Component.text("hat das Spiel gewonnen").color(NamedTextColor.GRAY),
                            Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ZERO)));
                else
                    player.showTitle(Title.title(team.registeredPlayers().get(0).displayName().color(StringData.getHighlightColor()),
                            Component.text("hat das Spiel gewonnen").color(NamedTextColor.GRAY),
                            Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ZERO)));
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 0);
                player.spawnParticle(Particle.TOTEM, player.getLocation().add(0, 1, 0), 250);
            }
        }

        Bukkit.getScheduler().runTaskLater(Bingo.getBingo(), () -> {
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
                Player winner = team.registeredPlayers().get(0);
                SpooklyPlayer spooklyPlayer = Spookly.getPlayer(winner.getUniqueId());

                winComponent.set(BigHeadMessage.getComponent(winner.getPlayer(), winner.displayName().color(StringData.getHighlightColor())
                        .append(Component.text(" hat das Spiel Gewonnen.").color(NamedTextColor.GRAY))));

                spooklyPlayer.addPoints(350);
                winner.sendMessage(TextComponentHelper.addPointsComponent(350));
                bingo.getUserFactory().updateWins(winner, UserFactory.UpdateType.ADD, 1);
            } else {
                winComponent.set(winComponent.get().append(team.prefix().font(TeamFont.KEY))
                        .append(Component.text(" hat das Spiel Gewonnen.").color(NamedTextColor.GRAY))
                        .append(TextComponentHelper.newLineComponent())
                        .append(StringData.getPrefix())
                        .append(Component.text("Zu diesem Team gehören:").color(NamedTextColor.GRAY)));

                team.registeredPlayers().forEach(player -> {
                    SpooklyPlayer spooklyPlayer = Spookly.getPlayer(player.getUniqueId());
                    winComponent.set(winComponent.get().append(Component.newline())
                            .append(Component.text("- ").color(NamedTextColor.GRAY))
                            .append(PlayerheadChatComponent.getHeadComponent(player, SmallPixelFont.class).font(SmallPixelFont.KEY))
                            .append(Component.text(" "))
                            .append(player.displayName()));

                    spooklyPlayer.addPoints(250);
                    player.sendMessage(TextComponentHelper.addPointsComponent(250));
                    Bingo.getBingo().getUserFactory().updateWins(player, UserFactory.UpdateType.ADD, 1);
                });
            }
            Bukkit.broadcast(winComponent.get().append(TextComponentHelper.newLineComponent()));
            startPhase();
        }, 100);
    }

    public void startPhase() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bingo.getBingo(), () -> {

            if (counter <= 10) {
                Bukkit.broadcast(StringData.getPrefix()
                        .append(Component.text("Der Server startet in ").color(NamedTextColor.GRAY))
                        .append(Component.text(counter + " Sekunden").color(StringData.getHighlightColor()))
                        .append(Component.text(" neu.").color(NamedTextColor.GRAY)));
            }
            if (counter == 0) {
                Bukkit.getScheduler().runTaskAsynchronously(Bingo.getBingo(), () -> {
                    for (final Player player : Bukkit.getOnlinePlayers()) {
                        SenderUtil.sendPlayerToGroup(player, "Lobby");
                    }
                });

            } else if (counter == -3) {
                Bukkit.getServer().shutdown();
            }
            counter--;
        }, 20, 20);
    }
}
