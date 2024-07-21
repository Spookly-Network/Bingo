package de.spookly.bingo.commands;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class hudCommand implements CommandExecutor {

	@Getter private static List<Player> betaPlayer = new ArrayList<>();
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		betaPlayer.add((Player) sender);
		return false;
	}
}
