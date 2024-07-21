package de.spookly.bingo.database;

import org.bson.Document;

import de.spookly.bingo.statistics.player.BingoPlayerStats;
import de.spookly.database.DatabaseComponentCodec;

public class BingoStatsCodec implements DatabaseComponentCodec<BingoPlayerStats> {
	@Override
	public Document encode(BingoPlayerStats bingoPlayerStats) {
		return new Document("gamesPlayed", bingoPlayerStats.getGamesPlayed())
				.append("gamesWin", bingoPlayerStats.getGamesWon())
				.append("itemsCompleted", bingoPlayerStats.getItemsCompleted())
				.append("brownSheep", bingoPlayerStats.getBrownSheep())
				.append("deaths", bingoPlayerStats.getDeaths())
				.append("distanceTraveled", bingoPlayerStats.getDistanceTraveled())
				.append("blocksBroken", bingoPlayerStats.getBlocksBroken())
				.append("itemsCrafted", bingoPlayerStats.getItemsCrafted());
	}

	@Override
	public BingoPlayerStats decode(Document document) {
		return BingoPlayerStats.builder()
				.gamesPlayed(document.getInteger("gamesPlayed"))
				.gamesWon(document.getInteger("gamesWon"))
				.itemsCompleted(document.getInteger("itemsCompleted"))
				.brownSheep(document.getInteger("brownSheep"))
				.deaths(document.getInteger("deaths"))
				.distanceTraveled(document.getInteger("distanceTraveled"))
				.blocksBroken(document.getInteger("blocksBroken"))
				.itemsCrafted(document.getInteger("itemsCrafted"))
				.build();
	}
}
