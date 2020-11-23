package de.zayon.bingo.commands;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
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
                int i = 0;
                for (Material m : GameData.getItemsToFind()) {
                    inv.setItem(i, Items.createItem(GameData.getItemsToFind().get(i), "", 1));
                    i++;
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
