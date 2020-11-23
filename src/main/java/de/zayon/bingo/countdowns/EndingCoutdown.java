package de.zayon.bingo.countdowns;

import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.data.helper.Team;
import de.zayon.bingo.factory.UserFactory;
import de.zayon.bingo.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EndingCoutdown {

    static int counter = 20;
    private final Bingo bingo;

    public EndingCoutdown(Bingo bingo) {
        this.bingo = bingo;
    }

    public static void teamWin(Team t) {

        GameState.state = GameState.END;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(GameData.getLobbyLocation());
            player.getInventory().clear();
            player.getInventory().setItem(8, Items.createSkullByUUID("§7Zurück zur Lobby", "50c8510b-5ea0-4d60-be9a-7d542d6cd156"));
        }

        Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + "Team-" + t.getTeamID() + " §7hat das Spiel Gewonnen");
        for (Player player : t.getMates()) {
            //TODO Add coins
//            CoinsAPI.addCoins(p, 250);
//            p.sendMessage(de.Zayon.API.Main.Main.Prefix + "Du hast §c250 &7Punkte erhalten.");
            Bingo.getBingo().getUserFactory().updateWins(player, UserFactory.UpdateType.ADD, 1);
        }
        Bukkit.getScheduler().cancelTasks(Bingo.getBingo());
        closeCountdown();
    }

    public static void closeCountdown() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bingo.getBingo(), new Runnable() {
            @Override
            public void run() {

                if(counter <= 10){
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
    }
}
