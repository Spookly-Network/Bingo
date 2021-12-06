package de.nehlen.bingo.sidebar;

import de.nehlen.bingo.Bingo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class SidebarListener implements Listener {
    private final Bingo bingo;

    public SidebarListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SidebarCache.removeCachedSidebar(player);
        this.bingo.getServer().getPluginManager().registerEvents(this, (Plugin)this.bingo);
    }
}
