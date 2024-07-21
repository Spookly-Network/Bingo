package de.spookly.bingo.listener;

import java.util.ArrayList;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.GameData;
import de.spookly.bingo.data.GameState;
import de.spookly.bingo.data.StringData;
import de.spookly.bingo.util.Items;
import de.spookly.player.PlayerRegisterEvent;
import de.spookly.player.SpooklyPlayer;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

	private final SpooklyBingoPlugin spooklyBingoPlugin;

	public PlayerJoinListener(SpooklyBingoPlugin spooklyBingoPlugin) {
		this.spooklyBingoPlugin = spooklyBingoPlugin;
	}

	@EventHandler
	public void handleJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.getInventory().clear();
		player.setHealthScale(20D);
		player.setHealth(20.0D);
		player.setFoodLevel(20);
		player.setGameMode(GameMode.ADVENTURE);
		event.joinMessage(Component.empty());

		Bukkit.getOnlinePlayers().forEach(this.spooklyBingoPlugin.getScoreboardManager()::setUserScoreboard);

		if (GameState.state == GameState.LOBBY) {
			Bukkit.broadcast(StringData.getPrefix()
					.append(Component.translatable("bingo.listener.join.message", player.displayName().color(StringData.getHighlightColor()))
							.color(NamedTextColor.GRAY)));
			player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA, Component.translatable("gamemode.general.lobby.backToLobby").color(NamedTextColor.GRAY), 1));
			if (GameData.getTeamSize() > 1) {
				player.getInventory().setItem(0, Items.createItem(Material.TOTEM_OF_UNDYING, Component.translatable("gamemode.general.lobby.teamSelect").color(NamedTextColor.GRAY), 1));
				player.getInventory().setItem(1, Items.createItem(Material.MAP, Component.translatable("gamemode.bingo.lobby.items").color(NamedTextColor.GRAY), 1));
			} else {
				player.getInventory().setItem(0, Items.createItem(Material.MAP, Component.translatable("gamemode.bingo.lobby.items").color(NamedTextColor.GRAY), 1));
			}

			ArrayList<Player> playerList = GameData.getIngame();
			playerList.add(player);
			GameData.setIngame(playerList);

			if (Bukkit.getOnlinePlayers().size() == GameData.getMinPlayerToStartGame()) {
				SpooklyBingoPlugin.getSpooklyBingoPlugin().getLobbyPhase().startPhase();
			}
			if (Bukkit.getOnlinePlayers().size() == (GameData.getTeamAmount() * GameData.getTeamSize()) / 2) {
				if (SpooklyBingoPlugin.getSpooklyBingoPlugin().getLobbyPhase().getCounter() >= 60) {
					SpooklyBingoPlugin.getSpooklyBingoPlugin().getLobbyPhase().setCounter(60);
				}
			}
			for (Player all : Bukkit.getOnlinePlayers()) {
				all.showPlayer(SpooklyBingoPlugin.getSpooklyBingoPlugin(), player);
			}
		} else if (GameState.state == GameState.INGAME) {
			player.setGameMode(GameMode.SURVIVAL);
			player.setFlying(true);
			player.setAllowFlight(true);
			player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA, Component.translatable("gamemode.general.lobby.backToLobby").color(NamedTextColor.GRAY), 1));
			player.getInventory().setItem(0, Items.createItem(Material.COMPASS, Component.translatable("gamemode.general.lobby.spectator").color(NamedTextColor.GRAY), 1));

			for (Player all : Bukkit.getOnlinePlayers()) {
				all.hidePlayer(SpooklyBingoPlugin.getSpooklyBingoPlugin(), player);
			}
			player.sendMessage(Component.newline()
					.append(Component.translatable("general.spectator.join").color(NamedTextColor.GRAY))
					.append(Component.translatable("general.spectator.start").color(NamedTextColor.GRAY))
					.append(Component.newline()));
		}
	}

	@EventHandler
	public void handlePlayerRegister(PlayerRegisterEvent event) {
		SpooklyPlayer player = event.getSpooklyPlayer();
		spooklyBingoPlugin.getPlayerStatisticsManager().loadPlayerStatistics(player);
	}

	@EventHandler
	public void handleSpawn(PlayerSpawnLocationEvent event) {
		Location location = GameData.getLobbyLocation();
		event.setSpawnLocation(location);
	}
}
