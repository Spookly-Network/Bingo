package de.nehlen.bingo.commands;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
import de.nehlen.gameapi.TeamAPI.Team;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class BackpackCommand implements CommandExecutor {

    private final Bingo bingo;

    public BackpackCommand(Bingo bingo) {
        this.bingo = bingo;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(GameData.getTeamSize() <= 1) {
            sender.sendMessage("Der Befehl geht nur im Team modi.");
            return false;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (GameState.state == GameState.INGAME) {
                if (GameData.getIngame().contains((Player) sender) && GameData.getTeamCache().containsKey((Player) sender)) {
                    Team team = GameData.getTeamCache().get((Player)sender);
                    Inventory inventory = team.teamInventory();
                    player.openInventory(inventory);
                }
            } else {
                sender.sendMessage(StringData.getPrefix().append(Component.text("Dieser Befehl kann nur wärend des Spiel ausgeführt werden.")));
            }
        } else {
            sender.sendMessage("Diesen Befehl kannst du nur im Spiel ausführen");
        }

        return false;
    }
}
