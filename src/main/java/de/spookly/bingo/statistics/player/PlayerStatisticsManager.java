package de.spookly.bingo.statistics.player;

import de.spookly.bingo.data.GameData;
import de.spookly.bingo.database.BingoStatsCodec;
import de.spookly.player.SpooklyOfflinePlayer;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatisticsManager {

    private Map<SpooklyOfflinePlayer, BingoPlayerStats> playerStats = new HashMap<SpooklyOfflinePlayer, BingoPlayerStats>();

    public void loadPlayerStatistics(SpooklyOfflinePlayer player) {
        BingoPlayerStats stats;
        if (player.hasDatabaseComponent(GameData.getDatabaseTableName())) {
            playerStats.put(player, player.getDatabaseComponent(GameData.getDatabaseTableName(), new BingoStatsCodec()));
            return;
        }
        playerStats.put(player, new BingoPlayerStats());
    }

    public BingoPlayerStats getPlayerStatistics(SpooklyOfflinePlayer player) {
        return playerStats.get(player);
    }

    public void savePlayerStatistics(SpooklyOfflinePlayer player) {
        if (player.hasDatabaseComponent(GameData.getDatabaseTableName())) {
            player.replaceDatabaseComponent(GameData.getDatabaseTableName(), playerStats.get(player), new BingoStatsCodec());
            return;
        }
        player.addDatabaseComponent(GameData.getDatabaseTableName(), playerStats.get(player), new BingoStatsCodec());
    }


}
