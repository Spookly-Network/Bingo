package de.zayon.bingo.commands;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.data.TeamData;
import de.zayon.bingo.data.helper.Team;
import de.zayon.bingo.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
                if (GameData.getIngame().contains((Player) commandSender) && TeamData.getPlayerTeamCache().containsKey((Player) commandSender)) {
                    int i = 0;
                    Team team = TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get((Player)commandSender));
                    for (Material m : GameData.getItemsToFind()) {
                        switch (i) {
                            case 0:
                                if (team.getPickList().getIt0()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 1:
                                if (team.getPickList().getIt1()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 2:
                                if (team.getPickList().getIt2()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 3:
                                if (team.getPickList().getIt3()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 4:
                                if (team.getPickList().getIt4()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 5:
                                if (team.getPickList().getIt5()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 6:
                                if (team.getPickList().getIt6()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 7:
                                if (team.getPickList().getIt7()) {
                                    m = Material.BARRIER;
                                }
                                break;
                            case 8:
                                if (team.getPickList().getIt8()) {
                                    m = Material.BARRIER;
                                }
                                break;
                        }

                        inv.setItem(i, Items.createItem(m, "", 1));
                        i++;
                    }
                } else {
                    int i = 0;
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
