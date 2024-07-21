package de.spookly.bingo.commands;

import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.StringData;
import de.spookly.bingo.data.helper.TextComponentHelper;
import de.spookly.bingo.statistics.player.BingoPlayerStats;
import de.spookly.Spookly;
import de.spookly.player.SpooklyPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StatsCommand implements CommandExecutor {

    private final SpooklyBingoPlugin spooklyBingoPlugin;
    public StatsCommand(SpooklyBingoPlugin spooklyBingoPlugin) {
        this.spooklyBingoPlugin = spooklyBingoPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Kann nur von Spielern ausgeführt werden.");
            return true;
        }

        Player player = ((Player) sender);
        if (args.length == 0) {
            //Self stats
            SpooklyPlayer sPlayer = Spookly.getPlayer(player);
            BingoPlayerStats stats = SpooklyBingoPlugin.getSpooklyBingoPlugin().getPlayerStatisticsManager().getPlayerStatistics(sPlayer);
            Component message = Component.empty()
                    .append(TextComponentHelper.newLineComponent())
                    .append(statisticsMessage(stats));

            sender.sendMessage(message);
            return true;
        } else if(args.length == 1) {
            //Another player stats
            Spookly.getOfflinePlayer(args[1], offlinePlayer -> {
                if (offlinePlayer == null) {
                    //TODO message better
                    sender.sendMessage("Spieler " + args[1] + " nicht gefunden.");
                }

                SpooklyBingoPlugin.getSpooklyBingoPlugin().getPlayerStatisticsManager().loadPlayerStatistics(offlinePlayer);
                BingoPlayerStats stats = SpooklyBingoPlugin.getSpooklyBingoPlugin().getPlayerStatisticsManager().getPlayerStatistics(offlinePlayer);

                Component message = Component.empty()
                        .append(TextComponentHelper.newLineComponent())
                        .append(Component.translatable("gamemode.bingo.statistics.fromPlayer", Component.text(args[1])).color(NamedTextColor.GRAY))
                        .append(statisticsMessage(stats));
                sender.sendMessage(message);
            });
            return true;
        }
        return false;
    }

    Component statisticsMessage(BingoPlayerStats statistics) {
        return Component.empty()
                .append(Component.translatable("gamemode.bingo.statistics.gamesPlayed").color(NamedTextColor.GRAY))
                .append(Component.text(statistics.getGamesPlayed()).color(StringData.getHighlightColor()))
                .append(TextComponentHelper.newLineComponent())
                .append(Component.translatable("gamemode.bingo.statistics.gamesWon").color(NamedTextColor.GRAY))
                .append(Component.text(statistics.getGamesWon()).color(StringData.getHighlightColor()))
                .append(TextComponentHelper.newLineComponent())
                .append(Component.translatable("gamemode.bingo.statistics.itemsCompleted").color(NamedTextColor.GRAY))
                .append(Component.text(statistics.getItemsCompleted()).color(StringData.getHighlightColor()))
                .append(TextComponentHelper.newLineComponent())
                .append(Component.translatable("gamemode.bingo.statistics.deaths").color(NamedTextColor.GRAY))
                .append(Component.text(statistics.getDeaths()).color(StringData.getHighlightColor()))
                .append(TextComponentHelper.newLineComponent())
                .append(Component.translatable("gamemode.bingo.statistics.brownSheep").color(NamedTextColor.GRAY))
                .append(Component.text(statistics.getBrownSheep()).color(StringData.getHighlightColor()))
                .append(TextComponentHelper.newLineComponent());
    }
}
