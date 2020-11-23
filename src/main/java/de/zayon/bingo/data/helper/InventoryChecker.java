package de.zayon.bingo.data.helper;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.countdowns.EndingCoutdown;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.data.TeamData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryChecker {

    public static void checkInv(Player player) {

        for (int i = 0; i < GameData.getItemsToFind().size(); i++) {
            Material material = GameData.getItemsToFind().get(i);
            if (player.getInventory().contains(material)) {
                Team team = TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(player));
                PickList list = team.getPickList();

                switch (i) {
                    case 0:
                        if (!list.getIt0()) {
                            list.setIt0(true);
                            TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(player)).setPickList(list);
                            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + "Team-" + team.getTeamID() + " §7hat " + StringData.getHighlightColor() + GameData.getItemsToFind().get(i).toString() + " §7gefunden.");

                        }
                        break;
                    case 1:
                        if (!list.getIt1()) {
                            list.setIt1(true);
                            TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(player)).setPickList(list);
                            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + "Team-" + team.getTeamID() + " §7hat " + StringData.getHighlightColor() + GameData.getItemsToFind().get(i).toString() + " §7gefunden.");

                        }
                        break;
                    case 2:
                        if (!list.getIt2()) {
                            list.setIt2(true);
                            TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(player)).setPickList(list);
                            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + "Team-" + team.getTeamID() + " §7hat " + StringData.getHighlightColor() + GameData.getItemsToFind().get(i).toString() + " §7gefunden.");

                        }
                        break;
                    case 3:
                        if (!list.getIt3()) {
                            list.setIt3(true);
                            TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(player)).setPickList(list);
                            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + "Team-" + team.getTeamID() + " §7hat " + StringData.getHighlightColor() + GameData.getItemsToFind().get(i).toString() + " §7gefunden.");

                        }
                        break;
                    case 4:
                        if (!list.getIt4()) {
                            list.setIt4(true);
                            TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(player)).setPickList(list);
                            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + "Team-" + team.getTeamID() + " §7hat " + StringData.getHighlightColor() + GameData.getItemsToFind().get(i).toString() + " §7gefunden.");

                        }
                        break;
                    case 5:
                        if (!list.getIt5()) {
                            list.setIt5(true);
                            TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(player)).setPickList(list);
                            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + "Team-" + team.getTeamID() + " §7hat " + StringData.getHighlightColor() + GameData.getItemsToFind().get(i).toString() + " §7gefunden.");

                        }
                        break;
                    case 6:
                        if (!list.getIt6()) {
                            list.setIt6(true);
                            TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(player)).setPickList(list);
                            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + "Team-" + team.getTeamID() + " §7hat " + StringData.getHighlightColor() + GameData.getItemsToFind().get(i).toString() + " §7gefunden.");

                        }
                        break;
                    case 7:
                        if (!list.getIt7()) {
                            list.setIt7(true);
                            TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(player)).setPickList(list);
                            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + "Team-" + team.getTeamID() + " §7hat " + StringData.getHighlightColor() + GameData.getItemsToFind().get(i).toString() + " §7gefunden.");

                        }
                        break;
                    case 8:
                        if (!list.getIt8()) {
                            list.setIt8(true);
                            TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(player)).setPickList(list);
                            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + "Team-" + team.getTeamID() + " §7hat " + StringData.getHighlightColor() + GameData.getItemsToFind().get(i).toString() + " §7gefunden.");

                        }
                        break;
                }

                if (list.check()) {
                    //TODO MUST BE CALLED SYNCED
                    Bukkit.getScheduler().runTask(Bingo.getBingo(), () -> {
                        EndingCoutdown.teamWin(team);
                    });
                }
            }
        }
    }
}
