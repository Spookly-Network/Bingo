package de.zayon.bingo.listener;

import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    private final Bingo bingo;

    public PlayerLoginListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void handleLogin(PlayerLoginEvent event) {

        if ((GameState.state == GameState.LOBBY) && (Bukkit.getOnlinePlayers().size() == Bukkit.getMaxPlayers())) {
            if (event.getPlayer().hasPermission("vip")) {

                int i = 0;

                if (Bukkit.getOnlinePlayers().size() == Bukkit.getMaxPlayers()) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (!players.hasPermission("vip")) {
                            event.getPlayer().kickPlayer(StringData.getPrefix() + "§7Du wurdest gekickt um einem §cPremium Spieler §7platz zu machen!");
                            i = 1;
                            break;
                        }
                    }
                }
                if (i == 0) {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, StringData.getPrefix() + "§7Es konnte kein Platz für dich gemacht werden!");
                } else {
                    event.allow();
                }
            }
            else {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, StringData.getPrefix() + "§7Kaufe dir §cPremium §7um volle Runden beitreten zu können!");
            }
        } else if(GameState.state == GameState.END) {
            event.getPlayer().sendMessage("§cDas Spiel startet derzeit neu.");
            BridgePlayerManager.getInstance().proxySendPlayer(BridgePlayerManager.getInstance().getOnlinePlayer(event.getPlayer().getUniqueId()), "Lobby-1");
        }
    }
}
