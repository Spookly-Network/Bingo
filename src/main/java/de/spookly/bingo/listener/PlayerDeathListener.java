package de.spookly.bingo.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import de.spookly.Spookly;
import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.GameData;
import de.spookly.bingo.data.StringData;
import de.spookly.bingo.statistics.player.BingoPlayerStats;
import de.spookly.player.SpooklyPlayer;
import de.spookly.team.Team;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathListener implements Listener {
	private final SpooklyBingoPlugin spooklyBingoPlugin;

	public PlayerDeathListener(SpooklyBingoPlugin spooklyBingoPlugin) {
		this.spooklyBingoPlugin = spooklyBingoPlugin;
	}

	@EventHandler
	public void handleDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		SpooklyPlayer spooklyPlayer = Spookly.getPlayer(player);
		BingoPlayerStats statistics = SpooklyBingoPlugin.getSpooklyBingoPlugin().getPlayerStatisticsManager().getPlayerStatistics(spooklyPlayer);

		statistics.setDeaths(statistics.getDeaths() + 1);
		event.deathMessage(StringData.getPrefix()
				.append(Component.translatable("gamemode.bingo.general.death", spooklyPlayer.nameTag())
						.color(NamedTextColor.GRAY)));

	}

	@EventHandler
	public void handleRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		Team team = GameData.getTeamCache().get(player);

		if (player.getRespawnLocation() == null) {
			event.setRespawnLocation((Location) team.memory().get("spawnLoc"));
		}
	}
}
