package de.spookly.bingo;

import de.spookly.bingo.data.GameData;
import de.spookly.team.Team;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Team team = GameData.getTeamCache().get((Player) sender);
        SpooklyBingoPlugin.getSpooklyBingoPlugin().getEndingPhase().teamWin(team);
        return false;
    }
}
