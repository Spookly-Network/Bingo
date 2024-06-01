package de.nehlen.bingo.listener;

import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.placeholder.PlaceholderContext;
import de.nehlen.spookly.player.SpooklyPlayer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncPlayerChatListener implements Listener {

    public AsyncPlayerChatListener() {}

    @EventHandler
    public void onChat(AsyncChatEvent e) {

        Player p = e.getPlayer();
        SpooklyPlayer spooklyPlayer = Spookly.getPlayer(p);

        e.setCancelled(true);

        for (Player players : Bukkit.getOnlinePlayers()) {
            PlaceholderContext context = new PlaceholderContext(players, PlaceholderContext.PlaceholderType.CHAT);

            if (GameState.state != GameState.END) {
                if (!GameData.getIngame().contains(p)) {
                    if (!GameData.getIngame().contains(players)) {
                        players.sendMessage(Component.text("✘ ").color(NamedTextColor.DARK_RED)
                                .append(spooklyPlayer.nameTag())
                                .append(Component.text(": ").color(NamedTextColor.GRAY))
                                .append(Spookly.getPlaceholderManager().replacePlaceholder(e.message(), context)));
                    }
                } else {
                    players.sendMessage(spooklyPlayer.nameTag()
                            .append(Component.text(": ").color(NamedTextColor.GRAY))
                            .append(Spookly.getPlaceholderManager().replacePlaceholder(e.message(), context)));
                }
            } else {
                players.sendMessage(spooklyPlayer.nameTag()
                        .append(Component.text(": ").color(NamedTextColor.GRAY))
                        .append(Spookly.getPlaceholderManager().replacePlaceholder(e.message(), context)));
            }
        }
    }
}

