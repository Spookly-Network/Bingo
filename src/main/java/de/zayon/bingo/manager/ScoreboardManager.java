package de.zayon.bingo.manager;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.*;
import de.zayon.bingo.data.helper.Team;
import de.zayon.bingo.sidebar.Sidebar;
import de.zayon.bingo.sidebar.SidebarCache;
import org.bukkit.Bukkit;
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
            }).runTaskTimer((Plugin) this.bingo, 0L, 60L));
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
        sidebar.setLines(scoreboardData.getLines(),
                "%kills%", Integer.valueOf(this.bingo.getUserFactory().getKills(player)),
                "%deaths%", Integer.valueOf(this.bingo.getUserFactory().getDeaths(player)),
                "%kd%", this.bingo.getUserFactory().getKDRatio(player),
                "%item1%", getText(player, 0),
                "%item2%", getText(player, 1),
                "%item3%", getText(player, 2),
                "%item4%", getText(player, 3),
                "%item5%", getText(player, 4),
                "%item6%", getText(player, 5),
                "%item7%", getText(player, 6),
                "%item8%", getText(player, 7),
                "%item9%", getText(player, 8),
                "%gamestatus%", GameState.state.toString()
        );
    }

    private String getText(Player player, int index) {
        Team team;
        if (!TeamData.getPlayerTeamCache().containsKey(player)) {
            team = TeamData.teamCache.get(0);
        } else {
            team = TeamData.teamCache.get(TeamData.getPlayerTeamCache().get(player));
        }

//        TeamData.teamCache.get(TeamData.getPlayerTeamCache().get(player));
        String itemName = GameData.getItemsToFind().get(index).toString();
        String color = "§c";

        switch (index) {
            case 0:
                if (team.getPickList().getIt0()) {
                    color = "§a";
                }
                break;
            case 1:
                if (team.getPickList().getIt1()) {
                    color = "§a";
                }
                break;
            case 2:
                if (team.getPickList().getIt2()) {
                    color = "§a";
                }
                break;
            case 3:
                if (team.getPickList().getIt3()) {
                    color = "§a";
                }
                break;
            case 4:
                if (team.getPickList().getIt4()) {
                    color = "§a";
                }
                break;
            case 5:
                if (team.getPickList().getIt5()) {
                    color = "§a";
                }
                break;
            case 6:
                if (team.getPickList().getIt6()) {
                    color = "§a";
                }
                break;
            case 7:
                if (team.getPickList().getIt7()) {
                    color = "§a";
                }
                break;
            case 8:
                if (team.getPickList().getIt8()) {
                    color = "§a";
                }
                break;
        }

        return color + itemName;
    }

}
