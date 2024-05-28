package de.nehlen.bingo.sidebar;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SidebarCache {
    private static final HashMap<Player, Sidebar> sidebarCache = new HashMap<>();

    private static int sidebarCacheSize;

    public static Sidebar getCachedSidebar(Player player, String defaultObjectiveName) {
        if (!sidebarCache.containsKey(player)) {
            Sidebar sidebar = new Sidebar(player, defaultObjectiveName, Component.text("board"));
            sidebarCache.put(player, sidebar);
            sidebar.display(player);
        }
        return sidebarCache.get(player);
    }

    public static Sidebar getUniqueCachedSidebar(Player player) {
        return getCachedSidebar(player, "board" + sidebarCacheSize++);
    }

    public static void removeCachedSidebar(Player player) {
        sidebarCache.remove(player);
    }
}
