package de.nehlen.bingo.commands;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.inventroy.BingoListInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
