package de.nehlen.bingo.commands;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.data.helper.TextComponentHelper;
import de.nehlen.bingo.factory.UserFactory;
import de.nehlen.gameapi.util.DatabaseLib;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StatsCommand implements CommandExecutor {

    private final Bingo bingo;
    public StatsCommand(Bingo bingo) {
        this.bingo = bingo;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player player = ((Player) sender);
            UserFactory userFactory = bingo.getUserFactory();
                Component message = TextComponentHelper.newLineComponent()
                        .append(Component.text("Gewonnene Spiele: ").color(NamedTextColor.GRAY))
                        .append(Component.text(userFactory.getWins(player)).color(StringData.getHighlightColor()))
                        .append(TextComponentHelper.newLineComponent())
                        .append(Component.text("Gespiele Spiele: ").color(NamedTextColor.GRAY))
                        .append(Component.text(userFactory.getGames(player)).color(StringData.getHighlightColor()))
                        .append(TextComponentHelper.newLineComponent())
                        .append(Component.text("Abgeschlossene Items: ").color(NamedTextColor.GRAY))
                        .append(Component.text(userFactory.getCraftedItems(player)).color(StringData.getHighlightColor()))
                        .append(TextComponentHelper.newLineComponent())
                        .append(Component.text("Tode: ").color(NamedTextColor.GRAY))
                        .append(Component.text(userFactory.getDeaths(player)).color(StringData.getHighlightColor()))
                        .append(TextComponentHelper.newLineComponent())
                        .append(Component.text("Getötete Braune-Schafe: ").color(NamedTextColor.GRAY))
                        .append(Component.text(userFactory.getBrownSheeps(player)).color(StringData.getHighlightColor()))
                        .append(TextComponentHelper.newLineComponent());
                player.sendMessage(message);
        } else {
            sender.sendMessage("Kann nur von Spielern ausgeführt werden.");
        }
        return false;
    }
}
