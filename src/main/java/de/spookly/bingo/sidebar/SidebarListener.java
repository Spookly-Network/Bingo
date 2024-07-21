package de.spookly.bingo.sidebar;

import de.spookly.bingo.SpooklyBingoPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class SidebarListener implements Listener {
    private final SpooklyBingoPlugin spooklyBingoPlugin;

    public SidebarListener(SpooklyBingoPlugin spooklyBingoPlugin) {
        this.spooklyBingoPlugin = spooklyBingoPlugin;
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SidebarCache.removeCachedSidebar(player);
        this.spooklyBingoPlugin.getServer().getPluginManager().registerEvents(this, (Plugin)this.spooklyBingoPlugin);
    }
}
