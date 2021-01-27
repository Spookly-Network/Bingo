package de.zayon.bingo.commands;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.data.helper.PickList;
import de.zayon.bingo.util.Items;
import de.zayon.zayonapi.TeamAPI.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BingoCommand implements CommandExecutor {

    private final Bingo bingo;

    public BingoCommand(Bingo bingo) {
        this.bingo = bingo;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Inventory inv = Bukkit.createInventory(null, 9, StringData.getHighlightColor() + "Diese Items musst du Sammeln");
            if (GameState.state == GameState.INGAME) {
                int i = 0;
                if (GameData.getIngame().contains((Player) commandSender) && GameData.getTeamCache().containsKey((Player) commandSender)) {
                    Team team = GameData.getTeamCache().get((Player)commandSender);
                    PickList pickList = (PickList) team.getMemory().get("picklist");
                    for (Material m : GameData.getItemsToFind()) {
                        switch (i) {
                            case 0:
                                if (pickList.getIt0()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 1:
                                if (pickList.getIt1()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 2:
                                if (pickList.getIt2()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 3:
                                if (pickList.getIt3()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 4:
                                if (pickList.getIt4()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 5:
                                if (pickList.getIt5()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 6:
                                if (pickList.getIt6()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 7:
                                if (pickList.getIt7()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 8:
                                if (pickList.getIt8()) {
                                    m = Material.BARRIER;
                                }
                                break;
                        }

                        inv.setItem(i, Items.createItem(m, "", 1));
                        i++;
                    }
                } else {
                    for (Material m : GameData.getItemsToFind()) {
                        inv.setItem(i, Items.createItem(GameData.getItemsToFind().get(i), "", 1));
                        i++;
                    }
                }
                ((Player) commandSender).openInventory(inv);
            } else {
                commandSender.sendMessage(StringData.getPrefix() + "Dieser Befehl kann nur wärend des Spiel ausgeführt werden.");
            }
        } else {
            commandSender.sendMessage("Diesen Befehl kannst du nur im Spiel ausführen");
        }

        return false;
    }
}
