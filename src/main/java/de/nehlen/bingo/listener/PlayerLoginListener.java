package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
import de.nehlen.spooklycloudnetutils.SenderUtil;
import de.nehlen.spooklycloudnetutils.SpooklyCloudNetUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
//        try {
            if ((GameState.state == GameState.LOBBY) && (Bukkit.getOnlinePlayers().size() == Bukkit.getMaxPlayers())) {
                if (event.getPlayer().hasPermission("vip")) {

                    int i = 0;

                    if (Bukkit.getOnlinePlayers().size() == Bukkit.getMaxPlayers()) {
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (!players.hasPermission("vip")) {
                                event.getPlayer().kick(StringData.getPrefix()
                                        .append(Component.text("Du wurdest gekickt um einem Premium Spieler platz zu machen!")).color(NamedTextColor.GRAY));
                                i = 1;
                                break;
                            }
                        }
                    }
                    if (i == 0) {
                        event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
                                StringData.getPrefix()
                                        .append(Component.text("Es konnte kein Platz für dich gemacht werden!")
                                                .color(NamedTextColor.GRAY)));
                    } else {
                        event.allow();
                    }
                } else {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
                            StringData.getPrefix()
                                    .append(Component.text("kaufe dir")).color(NamedTextColor.GRAY)
                                    .append(Component.text(" VIP ").color(NamedTextColor.GOLD))
                                    .append(Component.text("um volle Runden beitreten zu können.")
                                            .color(NamedTextColor.GRAY)));
                }
            } else if (GameState.state == GameState.END) {
                event.getPlayer().sendMessage("§cDas Spiel startet derzeit neu.");
                SenderUtil.sendPlayerToGroup(event.getPlayer(), "Lobby");
            }
//        } catch (Exception e) {
//            Sentry.captureException(e);
//        }
    }
}
