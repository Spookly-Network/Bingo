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

    public static void checkInv(final Player player) {

        Bukkit.getScheduler().runTaskAsynchronously(Bingo.getBingo(), new Runnable() {
            @Override
            public void run() {
                for (int i = 0; GameData.getItemsToFind().size() < i; i++) {
                    Material material = GameData.getItemsToFind().get(i);
                    if (player.getInventory().contains(material)) {
                        Team team = TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(player));
                        PickList list = team.getPickList();

                        switch (i) {
                            case 0:
                                if (!list.getIt0()) {
                                    list.setIt0(true);
                                }
                                break;
                            case 1:
                                if (!list.getIt1()) {
                                    list.setIt1(true);
                                }
                                break;
                            case 2:
                                if (!list.getIt2()) {
                                    list.setIt2(true);
                                }
                            case 3:
                                if (!list.getIt3()) {
                                    list.setIt3(true);
                                }
                                break;
                            case 4:
                                if (!list.getIt4()) {
                                    list.setIt4(true);
                                }
                                break;
                            case 5:
                                if (!list.getIt5()) {
                                    list.setIt5(true);
                                }
                            case 6:
                                if (!list.getIt6()) {
                                    list.setIt6(true);
                                }
                                break;
                            case 7:
                                if (!list.getIt7()) {
                                    list.setIt7(true);
                                }
                            case 8:
                                if (!list.getIt8()) {
                                    list.setIt8(true);
                                }
                                break;
                        }
                        TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(player)).setPickList(list);
                        Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + "Team-" + team.getTeamID() + " §7hat " + StringData.getHighlightColor() + GameData.getItemsToFind().get(i).toString() + " §7gefunden.");

                        if (list.check()) {
                            EndingCoutdown.teamWin(team);
                        }
                    }
                }
            }
        });
    }
}
