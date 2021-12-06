package de.nehlen.bingo.manager;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.countdowns.IngameCountdown;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.ScoreboardData;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.bingo.sidebar.Sidebar;
import de.nehlen.bingo.sidebar.SidebarCache;
import de.nehlen.gameapi.Gameapi;
import de.nehlen.gameapi.TeamAPI.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Locale;

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
                int sec = 0;

                public void run() {
                    ScoreboardManager.this.setScoreboardContent(player, this.counter);
                    if (sec >= 15) {
                        this.counter = ++this.counter % (ScoreboardData.values()).length;
                        sec = 0;
                    }
                    sec++;
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
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
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
                "%gamestatus%", GameState.state.toString(),
                "%timer%", getTime(IngameCountdown.counter),
                "%team%", getTeam(player)
        );
    }

    private String getTime(Integer seconds) {
        LocalTime timeOfDay = LocalTime.ofSecondOfDay(seconds);
        String time = timeOfDay.toString();
        return time;
    }

    private String getTeam(Player player) {
        if(GameData.getTeamCache().containsKey(player)) {
            return GameData.getTeamCache().get(player).getTeamName();
        } else {
            return "Kein Team";
        }
    }

    private String getText(Player player, int index) {
        Team team;
        if (!GameData.getTeamCache().containsKey(player)) {
            team = Gameapi.getGameapi().getTeamAPI().getRegisteredTeams().get(0);
        } else {
            team = GameData.getTeamCache().get(player);
        }
//        Bukkit.broadcastMessage(GameData.getItemsToFind().toString());
        String itemName = GameData.getItemsToFind().get(index).toString();
        String color = "§c";
        PickList pickList = (PickList) team.getMemory().get("picklist");


        switch (index) {
            case 0:
                if (pickList.getIt0()) {
                    color = "§a";
                }
                break;
            case 1:
                if (pickList.getIt1()) {
                    color = "§a";
                }
                break;
            case 2:
                if (pickList.getIt2()) {
                    color = "§a";
                }
                break;
            case 3:
                if (pickList.getIt3()) {
                    color = "§a";
                }
                break;
            case 4:
                if (pickList.getIt4()) {
                    color = "§a";
                }
                break;
            case 5:
                if (pickList.getIt5()) {
                    color = "§a";
                }
                break;
            case 6:
                if (pickList.getIt6()) {
                    color = "§a";
                }
                break;
            case 7:
                if (pickList.getIt7()) {
                    color = "§a";
                }
                break;
            case 8:
                if (pickList.getIt8()) {
                    color = "§a";
                }
                break;
        }

        return color + itemName.substring(0, 1).toUpperCase() + itemName.substring(1).replace("_", " ").toLowerCase(Locale.GERMANY);
    }

}
