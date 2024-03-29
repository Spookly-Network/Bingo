package de.nehlen.bingo.commands;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.bingo.data.helper.TranslatableHelper;
import de.nehlen.bingo.inventroy.BingoListInventory;
import de.nehlen.bingo.util.Items;
import de.nehlen.gameapi.TeamAPI.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BingoCommand implements CommandExecutor {

    private final Bingo bingo;

    public BingoCommand(Bingo bingo) {
        this.bingo = bingo;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            new BingoListInventory(((Player) commandSender).getPlayer()).open();
        } else {
            commandSender.sendMessage("Diesen Befehl kannst du nur im Spiel ausführen");
        }

        return false;
    }
}
