package de.spookly.bingo.listener;

import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.GameState;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildListener implements Listener {

	private final SpooklyBingoPlugin spooklyBingoPlugin;

	public BuildListener(SpooklyBingoPlugin spooklyBingoPlugin) {
		this.spooklyBingoPlugin = spooklyBingoPlugin;
	}

	@EventHandler
	public void handleBuild(BlockPlaceEvent event) {

		if(GameState.state != GameState.INGAME) {
			event.setCancelled(true);
		}

	}

	@EventHandler
	public void handleBuild(BlockBreakEvent event) {

		if(GameState.state != GameState.INGAME) {
			event.setCancelled(true);
		}

	}
}
