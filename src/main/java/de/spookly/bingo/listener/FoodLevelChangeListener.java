package de.spookly.bingo.listener;

import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.GameData;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {
	private final SpooklyBingoPlugin spooklyBingoPlugin;

	public FoodLevelChangeListener(SpooklyBingoPlugin spooklyBingoPlugin) {
		this.spooklyBingoPlugin = spooklyBingoPlugin;
	}
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		if(GameData.getIsHunger()) return;
		e.setCancelled(true);
	}
}
