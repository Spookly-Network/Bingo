package de.zayon.bingo.manager;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.ScoreboardData;
import de.zayon.bingo.sidebar.Sidebar;
import de.zayon.bingo.sidebar.SidebarCache;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class ScoreboardManager {
    private final Bingo bingo;

    private final HashMap<Player, BukkitTask> scoreboardTaskMap = new HashMap<>();

    private final int updateInterval = 1;

    public ScoreboardManager(Bingo bingo) {
        this.bingo = bingo;
    }

    public void setUserScoreboard(final Player player) {
        if (!this.scoreboardTaskMap.containsKey(player))
            this.scoreboardTaskMap.put(player, (new BukkitRunnable() {
                int counter = 0;

                public void run() {
                    ScoreboardManager.this.setScoreboardContent(player, this.counter);
                    this.counter = ++this.counter % (ScoreboardData.values()).length;
                }
            }).runTaskTimer((Plugin) this.bingo, 0L, 20L));
    }

    public void removeUserScoreboard(Player player) {
        if (this.scoreboardTaskMap.containsKey(player)) {
            ((BukkitTask) this.scoreboardTaskMap.get(player)).cancel();
            this.scoreboardTaskMap.remove(player);
        }
    }

    private void setScoreboardContent(Player player, int pageNumber) {
        ScoreboardData scoreboardData = ScoreboardData.values()[pageNumber];
        this.bingo.getSidebarCache();
        Sidebar sidebar = SidebarCache.getUniqueCachedSidebar(player);
        sidebar.setDisplayName(scoreboardData.getDisplayName());
        sidebar.setLines(scoreboardData.getLines(), "%kills%",
                Integer.valueOf(this.bingo.getUserFactory().getKills(player)), "%deaths%",
                Integer.valueOf(this.bingo.getUserFactory().getDeaths(player)), "%kd%", this.bingo.getUserFactory().getKDRatio(player));
    }
}
