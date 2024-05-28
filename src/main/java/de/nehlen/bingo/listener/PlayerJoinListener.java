package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.util.Items;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.ArrayList;

public class PlayerJoinListener implements Listener {

    private final Bingo bingo;

    public PlayerJoinListener(Bingo bingo) {
        this.bingo = bingo;
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

        this.bingo.getUserFactory().createUser(player);
        Bukkit.getOnlinePlayers().forEach(this.bingo.getScoreboardManager()::setUserScoreboard);

        if (GameState.state == GameState.LOBBY) {
            Bukkit.broadcast(StringData.getPrefix()
                    .append(Component.translatable("bingo.listener.join.message", player.displayName().color(StringData.getHighlightColor()))
                            .color(NamedTextColor.GRAY)));
            player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA, Component.translatable("gamemode.general.lobby.backToLobby").color(NamedTextColor.GRAY), 1));
            if (GameData.getTeamSize() > 1) {
                player.getInventory().setItem(0, Items.createItem(Material.TOTEM_OF_UNDYING, Component.translatable("gamemode.general.lobby.teamSelect").color(NamedTextColor.GRAY), 1));
                player.getInventory().setItem(1, Items.createItem(Material.CHEST, Component.translatable("gamemode.bingo.lobby.items").color(NamedTextColor.GRAY), 1));
            } else {
                player.getInventory().setItem(0, Items.createItem(Material.CHEST, Component.translatable("gamemode.bingo.lobby.items").color(NamedTextColor.GRAY), 1));
            }

            ArrayList<Player> playerList = GameData.getIngame();
            playerList.add(player);
            GameData.setIngame(playerList);

            if (Bukkit.getOnlinePlayers().size() == GameData.getMinPlayerToStartGame()) {
                Bingo.getBingo().getLobbyPhase().startPhase();
            }
            if (Bukkit.getOnlinePlayers().size() == (GameData.getTeamAmount() * GameData.getTeamSize()) / 2) {
                if (Bingo.getBingo().getLobbyPhase().getCounter() >= 60) {
                    Bingo.getBingo().getLobbyPhase().setCounter(60);
                }
            }
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.showPlayer(Bingo.getBingo(), player);
            }
        } else if (GameState.state == GameState.INGAME) {
            player.setGameMode(GameMode.SURVIVAL);
            player.setFlying(true);
            player.setAllowFlight(true);
            player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA, Component.translatable("gamemode.general.lobby.backToLobby").color(NamedTextColor.GRAY), 1));
            player.getInventory().setItem(0, Items.createItem(Material.COMPASS, Component.translatable("gamemode.general.lobby.spectator").color(NamedTextColor.GRAY), 1));

            for (Player all : Bukkit.getOnlinePlayers()) {
                all.hidePlayer(Bingo.getBingo(), player);
            }
            player.sendMessage(Component.newline()
                    .append(Component.translatable("general.spectator.join").color(NamedTextColor.GRAY))
                    .append(Component.translatable("general.spectator.start").color(NamedTextColor.GRAY))
                    .append(Component.newline()));
        }
    }

    @EventHandler
    public void handleSpawn(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        Location location = GameData.getLobbyLocation();
        event.setSpawnLocation(location);
    }
}
