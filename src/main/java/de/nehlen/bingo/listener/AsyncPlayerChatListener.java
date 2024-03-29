package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
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

        e.setCancelled(true);

        for(Player players: Bukkit.getOnlinePlayers()) {
            if(GameState.state != GameState.END) {
                if(!GameData.getIngame().contains(p)) {
                    if(!GameData.getIngame().contains(players))	{
                        players.sendMessage(Component.text("✘ ").color(NamedTextColor.DARK_RED)
                                .append(p.displayName())
                                .append(Component.text(": ").color(NamedTextColor.GRAY))
                                .append(e.message()));
                    }
                } else {
                    players.sendMessage(StringData.playerTeamPrefix(p).append(p.displayName())
                            .append(Component.text(": ").color(NamedTextColor.GRAY))
                            .append(e.message()));
                }
            } else {
                players.sendMessage(StringData.playerTeamPrefix(p).append(p.displayName())
                        .append(Component.text(": ").color(NamedTextColor.GRAY))
                        .append(e.message()));
            }
        }
    }
}

