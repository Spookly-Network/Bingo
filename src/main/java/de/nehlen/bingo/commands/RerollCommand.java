package de.nehlen.bingo.commands;

import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.bingo.phases.LobbyPhase;
import de.nehlen.spookly.Spookly;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RerollCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender.hasPermission("spookly.bingo.command.reroll")) {
            if(GameState.state == GameState.LOBBY) {
                List<Material> materialList = LobbyPhase.fillItemList();
                Spookly.getTeamManager().registeredTeams().forEach(team -> {
                    PickList picklist = new PickList(materialList);
                    team.memory().remove("picklist");
                    team.memory().put("picklist", picklist);
                });
                sender.sendMessage(StringData.getPrefix().append(Component.text("Picklist was rerolled.").color(NamedTextColor.GREEN)));
                return true;
            }
        }
        sender.sendMessage(StringData.combinate());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
