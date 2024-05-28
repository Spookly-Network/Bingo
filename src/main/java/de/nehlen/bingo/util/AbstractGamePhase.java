package de.nehlen.bingo.util;

import de.nehlen.bingo.Bingo;
import de.nehlen.spookly.phase.GamePhase;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;

@Accessors(fluent = true, chain = false)
public abstract class AbstractGamePhase implements GamePhase {

    @Getter
    @Setter
    protected Integer counter = 0;
    protected Integer scheduler = 0;

    public AbstractGamePhase(int counter) {
        this.counter = counter;
    }

    public void startPhase() {
        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bingo.getBingo(), () -> {}, 20L, 20L);
    };

    public void endPhase(){
        Bukkit.getScheduler().cancelTask(scheduler);
    };

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }
}
