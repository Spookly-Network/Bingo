package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.util.Items;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.generator.WorldInfo;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import javax.lang.model.element.Name;
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
        player.displayName(player.displayName().color(NamedTextColor.GRAY));
        player.playerListName(StringData.playerListName(player));
        event.joinMessage(Component.empty());

        this.bingo.getUserFactory().createUser(player);
        Bukkit.getOnlinePlayers().forEach(this.bingo.getScoreboardManager()::setUserScoreboard);

        if (GameState.state == GameState.LOBBY) {
//            player.teleport(GameData.getLobbyLocation());
            Bukkit.broadcast(StringData.getPrefix()
                    .append(player.displayName().color(StringData.getHighlightColor()))
                    .append(Component.text(" hat das Spiel betreten.").color(NamedTextColor.GRAY)));
            player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA, Component.text("Zurück zur Lobby").color(NamedTextColor.GRAY), 1));
            if(GameData.getTeamSize() > 1) {
                player.getInventory().setItem(0, Items.createItem(Material.TOTEM_OF_UNDYING, Component.text("Teamauswahl").color(NamedTextColor.GRAY), 1));
                player.getInventory().setItem(1, Items.createItem(Material.CHEST, Component.text("Gegenstände").color(NamedTextColor.GRAY), 1));
            } else {
                player.getInventory().setItem(0, Items.createItem(Material.CHEST, Component.text("Gegenstände").color(NamedTextColor.GRAY), 1));
            }
            player.updateInventory();

            ArrayList<Player> playerList = GameData.getIngame();
            playerList.add(player);
            GameData.setIngame(playerList);

            if (Bukkit.getOnlinePlayers().size() == GameData.getMinPlayerToStartGame()) {
                Bingo.getBingo().getLobbyCountdown().startPhase();
            }
            if (Bukkit.getOnlinePlayers().size() == (GameData.getTeamAmount() * GameData.getTeamSize()) / 2) {
                if (Bingo.getBingo().getLobbyCountdown().getCounter() >= 60) {
                    Bingo.getBingo().getLobbyCountdown().setCounter(60);
                }
            }
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.showPlayer(Bingo.getBingo(), player);
            }
        } else if (GameState.state == GameState.INGAME) {
//            player.setGameMode(GameMode.SURVIVAL);
//            TODO; Make Spectator compass;
            player.setGameMode(GameMode.SPECTATOR);
            player.setFlying(true);
            player.setAllowFlight(true);
            player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA, Component.text("Zurück zur Lobby").color(NamedTextColor.GRAY), 1));
            player.getInventory().setItem(0, Items.createItem(Material.COMPASS, Component.text("Spieler").color(NamedTextColor.GRAY), 1));

            for (Player all : Bukkit.getOnlinePlayers()) {
                all.hidePlayer(Bingo.getBingo(), player);
            }
            player.sendMessage("");
            player.sendMessage("§7Diese Runde kannst du nur noch Zuschauen!");
            player.sendMessage("§7Nutze den Kompass um dich zu einem Spieler zu teleportieren.");
        }
    }

    @EventHandler
    public void handleSpawn(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        Location location = GameData.getLobbyLocation();
        event.setSpawnLocation(location);
    }
}
