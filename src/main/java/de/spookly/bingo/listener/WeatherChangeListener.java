package de.spookly.bingo.listener;

import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.GameState;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

	private final SpooklyBingoPlugin spooklyBingoPlugin;

	public WeatherChangeListener(SpooklyBingoPlugin spooklyBingoPlugin) {
		this.spooklyBingoPlugin = spooklyBingoPlugin;
	}

	@EventHandler
	public void handleWeatherChange(WeatherChangeEvent event) {
		if(GameState.state != GameState.INGAME)
			event.setCancelled(true);
	}
}
