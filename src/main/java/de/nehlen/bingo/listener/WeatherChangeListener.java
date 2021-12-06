package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    private final Bingo bingo;

    public WeatherChangeListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void handleWeatherChange(WeatherChangeEvent event) {
        if(GameState.state != GameState.INGAME)
            event.setCancelled(true);
    }
}
