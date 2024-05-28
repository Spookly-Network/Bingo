package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.player.SpooklyPlayer;
import de.nehlen.spookly.team.Team;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {
    private final Bingo bingo;

    public AsyncPlayerChatListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void onChat(AsyncChatEvent e) {

        Player p = e.getPlayer();
        SpooklyPlayer spooklyPlayer = Spookly.getPlayer(p);

        e.setCancelled(true);

        for (Player players : Bukkit.getOnlinePlayers()) {

            if (GameState.state != GameState.END) {
                if (!GameData.getIngame().contains(p)) {
                    if (!GameData.getIngame().contains(players)) {
                        players.sendMessage(Component.text("✘ ").color(NamedTextColor.DARK_RED)
                                .append(spooklyPlayer.nameTag())
                                .append(Component.text(": ").color(NamedTextColor.GRAY))
                                .append(Spookly.getPlaceholderManager().replacePlaceholder(e.message(), p)));
                    }
                } else {
                    Team team = GameData.getTeamCache().get(players);
                    players.sendMessage(spooklyPlayer.nameTag()
                            .append(Component.text(": ").color(NamedTextColor.GRAY))
                            .append(Spookly.getPlaceholderManager().replacePlaceholder(e.message(), p)));
                }
            } else {
                players.sendMessage(spooklyPlayer.nameTag()
                        .append(Component.text(": ").color(NamedTextColor.GRAY))
                        .append(Spookly.getPlaceholderManager().replacePlaceholder(e.message(), p)));
            }
        }
    }
}

