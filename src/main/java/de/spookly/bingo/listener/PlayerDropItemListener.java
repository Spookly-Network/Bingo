package de.spookly.bingo.listener;

import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.GameState;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

	private final SpooklyBingoPlugin spooklyBingoPlugin;

	public PlayerDropItemListener(SpooklyBingoPlugin spooklyBingoPlugin) {
		this.spooklyBingoPlugin = spooklyBingoPlugin;
	}

	@EventHandler
	public void handleItemDrop(PlayerDropItemEvent event) {

		Player player = event.getPlayer();
		if(GameState.state == GameState.LOBBY || GameState.state == GameState.END) {
			event.setCancelled(true);
		}
	}
}
