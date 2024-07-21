package de.spookly.bingo.commands;

import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.inventroy.BingoListInventory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BingoCommand implements CommandExecutor {

	private final SpooklyBingoPlugin spooklyBingoPlugin;

	public BingoCommand(SpooklyBingoPlugin spooklyBingoPlugin) {
		this.spooklyBingoPlugin = spooklyBingoPlugin;
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
