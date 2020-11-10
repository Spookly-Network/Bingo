package de.zayon.bingo.countdowns;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.data.helper.InventoryChecker;
import de.zayon.bingo.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class IngameCountdown {

    public static int counter = GameData.getMaxGameTime();
    private final Bingo bingo;
    public IngameCountdown(Bingo bingo) {
        this.bingo = bingo;
    }

    public static void ingameCountdown() {

        Bukkit.getScheduler().cancelTasks(Bingo.getBingo());


        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bingo.getBingo(), new Runnable() {
            @Override
            public void run() {


                Bukkit.getScheduler().runTaskAsynchronously(Bingo.getBingo(), new Runnable() {
                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            InventoryChecker.checkInv(player);
                        }
                    }
                });

//                if(counter != 0) {
//                    for (Player p : Bukkit.getOnlinePlayers()) {
//                        GameVars.checkInv(p);
//                    }
//                }
                if(counter == 0) {
                    Bukkit.broadcastMessage(StringData.getPrefix() + "Das Spiel ist zuende, es gibt keinen Gewinner.");
                    GameState.state = GameState.END;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.teleport(GameData.getLobbyLocation());
                        player.getInventory().clear();
                        player.getInventory().setItem(8, Items.createSkull("§7Zurück zur Lobby", "MHF_ArrowRight"));
                    }
                    EndingCoutdown.closeCountdown();
                }
                counter--;
            }
        }, 20L, 20L);
    }

}
