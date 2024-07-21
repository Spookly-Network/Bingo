package de.nehlen.bingo.statistics.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BingoPlayerStats {
    private Integer gamesPlayed;
    private Integer gamesWon;
    private Integer itemsCompleted;
    private Integer brownSheep;
    private Integer deaths;

    //More specific statistics
    private Integer distanceTraveled;
    private Integer blocksBroken;
    private Integer itemsCrafted;

    protected BingoPlayerStats() {
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.itemsCompleted = 0;
        this.brownSheep = 0;
        this.deaths = 0;
        this.distanceTraveled = 0;
        this.blocksBroken = 0;
        this.itemsCrafted = 0;
    }
}
