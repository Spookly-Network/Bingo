package de.zayon.bingo.listener;

import de.zayon.bingo.Bingo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    private final Bingo bingo;

    public WeatherChangeListener(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void handleWeatherChange(WeatherChangeEvent event) {event.setCancelled(true);}
}
