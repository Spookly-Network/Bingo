package de.zayon.bingo.countdowns;

import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.data.helper.Team;
import de.zayon.bingo.factory.UserFactory;
import de.zayon.bingo.util.Items;
import de.zayon.zayonapi.PointsAPI.PointsAPI;
import de.zayon.zayonapi.ZayonAPI;
import de.zayon.zayonapi.ZayonAPI;
import io.sentry.Sentry;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EndingCoutdown {

    static int counter = 20;
    private final Bingo bingo;

    public EndingCoutdown(Bingo bingo) {
        this.bingo = bingo;
    }

    public static void teamWin(Team t) {

        try {
            GameState.state = GameState.END;
            Bukkit.getScheduler().cancelTask(IngameCountdown.ingameCounter);
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(StringData.getHighlightColor() + "Team-" + (t.getTeamID() + 1), " §7hat das Spiel Gewonnen", 20, 60, 0);
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 0);
                player.spawnParticle(Particle.TOTEM, player.getLocation().add(0, 1, 0), 250);
            }

            Bukkit.getScheduler().runTaskLater(Bingo.getBingo(), () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.teleport(GameData.getLobbyLocation());
                    player.getInventory().clear();
                    player.getInventory().setItem(8, Items.createSkullByUUID("§7Zurück zur Lobby", "50c8510b-5ea0-4d60-be9a-7d542d6cd156"));
                }
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + "Team-" + (t.getTeamID() + 1) + " §7hat das Spiel Gewonnen");
                Bukkit.broadcastMessage(StringData.getPrefix() + "Zu diesem Team gehören:");
                for (Player player : t.getMates()) {
                    Bukkit.broadcastMessage("§7- " + player.getDisplayName());
                    //TODO Add coins

                    ZayonAPI.getZayonAPI().getPointsAPI().updatePoints(player, PointsAPI.UpdateType.ADD, 250);
                    player.sendMessage(StringData.getPrefix() + "Du hast §c250 §7Punkte erhalten.");

                    Bingo.getBingo().getUserFactory().updateWins(player, UserFactory.UpdateType.ADD, 1);
                }
                Bukkit.broadcastMessage("");
                Bukkit.getScheduler().cancelTasks(Bingo.getBingo());
                closeCountdown();
            }, 100);
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }

    public static void closeCountdown() {
        try {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(Bingo.getBingo(), new Runnable() {
                @Override
                public void run() {

                    if (counter <= 10) {
                        Bukkit.broadcastMessage(StringData.getPrefix() + "Der Server startet in " + StringData.getHighlightColor() + counter + " Sekunden §7neu.");
                    }
                    if (counter == 0) {
                        Bukkit.getScheduler().runTaskAsynchronously(Bingo.getBingo(), new Runnable() {
                            @Override
                            public void run() {
                                for (final Player player : Bukkit.getOnlinePlayers()) {
                                    BridgePlayerManager.getInstance().proxySendPlayer(BridgePlayerManager.getInstance().getOnlinePlayer(player.getUniqueId()), "Lobby-1");
                                }
                            }
                        });

                    } else if (counter == -3) {
                        Bukkit.getServer().shutdown();
                    }
                    counter--;
                }
            }, 20, 20);
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }
}
