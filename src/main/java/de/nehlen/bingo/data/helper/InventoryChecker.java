package de.nehlen.bingo.data.helper;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.countdowns.EndingCoutdown;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.StringData;
import de.nehlen.gameapi.Gameapi;
import de.nehlen.gameapi.PointsAPI.PointsAPI;
import de.nehlen.gameapi.TeamAPI.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Locale;

public class InventoryChecker {

    public static void checkInv(Player player) {

        for (int i = 0; i < GameData.getItemsToFind().size(); i++) {
            Material material = GameData.getItemsToFind().get(i);
            if (player.getInventory().contains(material)) {
                Team team = GameData.getTeamCache().get(player);
                PickList list = (PickList) team.getMemory().get("picklist");

                switch (i) {
                    case 0:
                        if (!list.getIt0()) {
                            list.setIt0(true);
                            action(player, i, team, list);

                        }
                        break;
                    case 1:
                        if (!list.getIt1()) {
                            list.setIt1(true);
                            action(player, i, team, list);
                        }
                        break;
                    case 2:
                        if (!list.getIt2()) {
                            list.setIt2(true);
                            action(player, i, team, list);

                        }
                        break;
                    case 3:
                        if (!list.getIt3()) {
                            list.setIt3(true);
                            action(player, i, team, list);

                        }
                        break;
                    case 4:
                        if (!list.getIt4()) {
                            list.setIt4(true);
                            action(player, i, team, list);

                        }
                        break;
                    case 5:
                        if (!list.getIt5()) {
                            list.setIt5(true);
                            action(player, i, team, list);

                        }
                        break;
                    case 6:
                        if (!list.getIt6()) {
                            list.setIt6(true);
                            action(player, i, team, list);

                        }
                        break;
                    case 7:
                        if (!list.getIt7()) {
                            list.setIt7(true);
                            action(player, i, team, list);

                        }
                        break;
                    case 8:
                        if (!list.getIt8()) {
                            list.setIt8(true);
                            action(player, i, team, list);

                        }
                        break;
                }

                if (list.check()) {
                    Bukkit.getScheduler().runTask(Bingo.getBingo(), () -> {
                        EndingCoutdown.teamWin(team);
                    });
                }
            }
        }
    }

    private static void action(Player player, int i, Team team, PickList list) {
        Gameapi.getGameapi().getPointsAPI().updatePoints(player, PointsAPI.UpdateType.ADD, 100);
        player.sendMessage(StringData.getPrefix() + "Du hast §c100 §7Punkte erhalten.");

        team.replaceFromMemory("picklist", list);
        String itemName = GameData.getItemsToFind().get(i).toString();
        Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + team.getTeamName() + " §7hat " + StringData.getHighlightColor() + itemName.substring(0, 1).toUpperCase() + itemName.substring(1).replace("_", " ").toLowerCase(Locale.GERMANY) + " §7gefunden.");
    }
}
