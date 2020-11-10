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
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.teleport(GameData.getLobbyLocation());
            p.getInventory().clear();
            p.getInventory().setItem(8, Items.createSkull("§7Zurück zur Lobby", "MHF_ArrowRight"));
        }

        Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + "Team-" + t.getTeamID() + " §7hat das Spiel Gewonnen");
        for (Player player : t.getMates()) {
            //TODO Add coins
//            CoinsAPI.addCoins(p, 250);
//            p.sendMessage(de.Zayon.API.Main.Main.Prefix + "Du hast §c250 &7Punkte erhalten.");
            Bingo.getBingo().getUserFactory().updateWins(player, UserFactory.UpdateType.ADD, 1);
        }
        closeCountdown();
    }

    public static void closeCountdown() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bingo.getBingo(), new Runnable() {
            @Override
            public void run() {

                if(counter <= 10){
                    Bukkit.broadcastMessage(StringData.getPrefix() + "Der Server startet in " + StringData.getHighlightColor() + counter + " Sekunden &7neu.");
                }
                if (counter == 0) {
//                    final Collection<? extends Player> onlinePlayer = Bukkit.getOnlinePlayers();
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
