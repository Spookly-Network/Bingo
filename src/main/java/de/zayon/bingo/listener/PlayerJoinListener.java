package de.zayon.bingo.listener;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.countdowns.LobbyCountdown;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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
        event.setJoinMessage("");

        this.bingo.getUserFactory().createUser(player);
        Bukkit.getOnlinePlayers().forEach(this.bingo.getScoreboardManager()::setUserScoreboard);

        if (GameState.state == GameState.LOBBY) {

//            player.teleport(GameData.getLobbyLocation());
            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + player.getDisplayName() + " §7hat das Spiel betreten.");
            player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA,"§7Zurück zur Lobby", 1));
            player.getInventory().setItem(0, Items.createItem(Material.TOTEM_OF_UNDYING, "§7Teamauswahl", 1));
            player.updateInventory();

            ArrayList playerList = GameData.getIngame();
            playerList.add(player);
            GameData.setIngame(playerList);

            if (player.hasPermission("bingo.start") || player.hasPermission("vip")) {
                player.getInventory().setItem(1, Items.createItem(Material.DIAMOND, "§7Starte das Spiel", 1));
            }
            if (Bukkit.getOnlinePlayers().size() == 2) {
                this.bingo.getLobbyCountdown().startLobbyCountdown(false);
            }
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.showPlayer(Bingo.getBingo(), player);
            }
        } else if (GameState.state == GameState.INGAME) {

//            player.teleport(GameData.getLobbyLocation());
            player.setGameMode(GameMode.SURVIVAL);
            player.setFlying(true);
            player.setAllowFlight(true);
            player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA,"§7Zurück zur Lobby", 1));
            player.getInventory().setItem(0, Items.createItem(Material.COMPASS, "§7Spieler", 1));

            for (Player all : Bukkit.getOnlinePlayers()) {
                all.hidePlayer(Bingo.getBingo(), player);
            }
            player.sendMessage("");
            player.sendMessage("§7Diese Runde kannst du nur noch Zuschauen!");
            player.sendMessage("§7Nutze um zu den Kompass um dich zu einem Spieler zu teleportieren.");
        }
    }

    @EventHandler
    public void handleSpawn(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        Location location = GameData.getLobbyLocation();
        event.setSpawnLocation(location);
    }
}
