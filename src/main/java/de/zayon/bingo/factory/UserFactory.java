package de.zayon.bingo.factory;

import de.zayon.bingo.Bingo;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.concurrent.CompletableFuture;

public class UserFactory {
    private final Bingo bingo;

    public UserFactory(Bingo bingo) {
        this.bingo = bingo;
    }

    public void createTable() {
        StringBuilder table = new StringBuilder();
        table.append("id INT(11) NOT NULL AUTO_INCREMENT, ");
        table.append("`uuid` VARCHAR(64) NOT NULL UNIQUE, ");
        table.append("`kills` INT(11) NOT NULL, ");
        table.append("`deaths` INT(11) NOT NULL, ");
        table.append("`games` INT(11) NOT NULL, ");
        table.append("`wins` INT(11) NOT NULL, ");
        table.append("PRIMARY KEY (`id`)");
        this.bingo.getDatabaseLib().executeUpdateAsync("CREATE TABLE IF NOT EXISTS zayon_bingo_stats (" + table.toString() + ")", resultSet -> {});
    }

    public CompletableFuture<Boolean> userExists(Player player) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        this.bingo.getDatabaseLib().executeQueryAsync("SELECT id FROM zayon_bingo_stats WHERE uuid = ?", resultSet -> {
            try {
                completableFuture.complete(Boolean.valueOf(resultSet.next()));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }, player.getUniqueId().toString());
        return completableFuture;
    }

    public void createUser(Player player) {
        userExists(player).whenCompleteAsync((exist, throwable) -> {
            if (throwable == null && !exist.booleanValue())
                this.bingo.getDatabaseLib().executeUpdateAsync("INSERT INTO zayon_bingo_stats (uuid, kills, deaths) VALUES (?, ?, ?)", resultSet -> {}, player.getUniqueId().toString(), Integer.valueOf(0), Integer.valueOf(0));
        });
    }

    public int getKills(Player player) {
        return ((Integer)this.bingo.getDatabaseLib().get("SELECT kills FROM zayon_bingo_stats WHERE uuid = ?", player.getUniqueId().toString(), "kills")).intValue();
    }

    public int getDeaths(Player player) {
        return ((Integer)this.bingo.getDatabaseLib().get("SELECT deaths FROM zayon_bingo_stats WHERE uuid = ?", player.getUniqueId().toString(), "deaths")).intValue();
    }

    public int getWins(Player player) {
        return ((Integer)this.bingo.getDatabaseLib().get("SELECT wins FROM zayon_bingo_stats WHERE uuid = ?", player.getUniqueId().toString(), "wins")).intValue();
    }

    public int getGames(Player player) {
        return ((Integer)this.bingo.getDatabaseLib().get("SELECT games FROM zayon_bingo_stats WHERE uuid = ?", player.getUniqueId().toString(), "games")).intValue();
    }

    public void updateKills(Player player, UpdateType updateType, int kills) {
        int newKills = 0;
        if (updateType == UpdateType.ADD) {
            newKills = getKills(player) + kills;
        } else if (updateType == UpdateType.REMOVE) {
            newKills = getKills(player) - kills;
        }
        this.bingo.getDatabaseLib().executeUpdateAsync("UPDATE zayon_bingo_stats SET kills = ? WHERE uuid = ?", resultSet -> {},Integer.valueOf(newKills), player.getUniqueId().toString() );
    }

    public void updateDeaths(Player player, UpdateType updateType, int deaths) {
        int newDeaths = 0;
        if (updateType == UpdateType.ADD) {
            newDeaths = getDeaths(player) + deaths;
        } else if (updateType == UpdateType.REMOVE) {
            newDeaths = getDeaths(player) - deaths;
        }
        this.bingo.getDatabaseLib().executeUpdateAsync("UPDATE zayon_knockbackffa_stats SET deaths = ? WHERE uuid = ?", resultSet -> {}, Integer.valueOf(newDeaths), player.getUniqueId().toString());
    }

    public void updateWins(Player player, UpdateType updateType, int wins) {
        int newWins = 0;
        if (updateType == UpdateType.ADD) {
            newWins = getKills(player) + wins;
        } else if (updateType == UpdateType.REMOVE) {
            newWins = getKills(player) - wins;
        }
        this.bingo.getDatabaseLib().executeUpdateAsync("UPDATE zayon_bingo_stats SET wins = ? WHERE uuid = ?", resultSet -> {},Integer.valueOf(newWins), player.getUniqueId().toString() );
    }

    public void updateGames(Player player, UpdateType updateType, int games) {
        int newGames = 0;
        if (updateType == UpdateType.ADD) {
            newGames = getKills(player) + games;
        } else if (updateType == UpdateType.REMOVE) {
            newGames = getKills(player) - games;
        }
        this.bingo.getDatabaseLib().executeUpdateAsync("UPDATE zayon_bingo_stats SET games = ? WHERE uuid = ?", resultSet -> {},Integer.valueOf(newGames), player.getUniqueId().toString() );
    }

    public String getKDRatio(Player player) {
        double kd = (double) getKills(player) / (double) getDeaths(player);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(kd);
    }


    public enum UpdateType {
        ADD, REMOVE;
    }
}
