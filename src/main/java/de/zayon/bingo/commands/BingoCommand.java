package de.zayon.bingo.commands;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.StringData;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BingoCommand implements CommandExecutor {

    private final Bingo bingo;

    public BingoCommand(Bingo bingo) {
        this.bingo = bingo;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        return false;
    }
}
