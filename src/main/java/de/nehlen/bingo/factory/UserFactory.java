package de.nehlen.bingo.factory;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.StringData;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.UUID;
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
        table.append("`items` INT(11) NOT NULL, ");
        table.append("`deaths` INT(11) NOT NULL, ");
        table.append("`games` INT(11) NOT NULL, ");
        table.append("`wins` INT(11) NOT NULL, ");
        table.append("`brownSheeps` INT(11) NOT NULL, ");
        table.append("PRIMARY KEY (`id`)");
        this.bingo.getDatabaseLib().executeUpdateAsync("CREATE TABLE IF NOT EXISTS " + GameData.getDatabaseTableName() + " (" + table.toString() + ")", resultSet -> {
        });
    }

    public CompletableFuture<Boolean> userExists(Player player) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        this.bingo.getDatabaseLib().executeQueryAsync("SELECT id FROM " + GameData.getDatabaseTableName() + " WHERE uuid = ?", resultSet -> {
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
                this.bingo.getDatabaseLib().executeUpdateAsync("INSERT INTO " + GameData.getDatabaseTableName() + " (uuid, items, deaths, games, wins, brownSheeps) VALUES (?, ?, ?, ?, ?, ?)", resultSet -> {
                }, player.getUniqueId().toString(), 0, 0, 0, 0, 0);
        });
    }

    public int getCraftedItems(Player player) {
        return (Integer) this.bingo.getDatabaseLib().get("SELECT items FROM " + GameData.getDatabaseTableName() + " WHERE uuid = ?", player.getUniqueId().toString(), "items");
    }

    public int getDeaths(Player player) {
        return (Integer) this.bingo.getDatabaseLib().get("SELECT deaths FROM " + GameData.getDatabaseTableName() + " WHERE uuid = ?", player.getUniqueId().toString(), "deaths");
    }

    public int getWins(Player player) {
        return (Integer) this.bingo.getDatabaseLib().get("SELECT wins FROM " + GameData.getDatabaseTableName() + " WHERE uuid = ?", player.getUniqueId().toString(), "wins");
    }

    public int getWins(UUID uuid) {
        return (Integer) this.bingo.getDatabaseLib().get("SELECT wins FROM " + GameData.getDatabaseTableName() + " WHERE uuid = ?", uuid.toString(), "wins");
    }


    public int getGames(Player player) {
        return (Integer) this.bingo.getDatabaseLib().get("SELECT games FROM " + GameData.getDatabaseTableName() + " WHERE uuid = ?", player.getUniqueId().toString(), "games");
    }

    public int getGames(UUID uuid) {
        return (Integer) this.bingo.getDatabaseLib().get("SELECT games FROM " + GameData.getDatabaseTableName() + " WHERE uuid = ?", uuid.toString(), "games");
    }

    public int getBrownSheeps(Player player) {
        return (Integer) this.bingo.getDatabaseLib().get("SELECT brownSheeps FROM " + GameData.getDatabaseTableName() + " WHERE uuid = ?", player.getUniqueId().toString(), "brownSheeps");
    }

    public void updateCraftedItems(Player player, UpdateType updateType, int items) {
        if (!GameData.getStats()) return;
        int newItems = 0;
        if (updateType == UpdateType.ADD) {
            newItems = getCraftedItems(player) + items;
        } else if (updateType == UpdateType.REMOVE) {
            newItems = getCraftedItems(player) - items;
        }
        this.bingo.getDatabaseLib().executeUpdateAsync("UPDATE " + GameData.getDatabaseTableName() + " SET items = ? WHERE uuid = ?", resultSet -> {
        }, newItems, player.getUniqueId().toString());
    }

    public void updateDeaths(Player player, UpdateType updateType, int deaths) {
        if (!GameData.getStats()) return;
        int newDeaths = 0;
        if (updateType == UpdateType.ADD) {
            newDeaths = getDeaths(player) + deaths;
        } else if (updateType == UpdateType.REMOVE) {
            newDeaths = getDeaths(player) - deaths;
        }
        this.bingo.getDatabaseLib().executeUpdateAsync("UPDATE " + GameData.getDatabaseTableName() + " SET deaths = ? WHERE uuid = ?", resultSet -> {
        }, newDeaths, player.getUniqueId().toString());
    }

    public void updateWins(Player player, UpdateType updateType, int wins) {
        if (!GameData.getStats()) return;
        int newWins = 0;
        if (updateType == UpdateType.ADD) {
            newWins = getWins(player) + wins;
        } else if (updateType == UpdateType.REMOVE) {
            newWins = getWins(player) - wins;
        }
        this.bingo.getDatabaseLib().executeUpdateAsync("UPDATE " + GameData.getDatabaseTableName() + " SET wins = ? WHERE uuid = ?", resultSet -> {
        }, newWins, player.getUniqueId().toString());
    }

    public void updateGames(Player player, UpdateType updateType, int games) {
        if (!GameData.getStats()) return;
        int newGames = 0;
        if (updateType == UpdateType.ADD) {
            newGames = getGames(player) + games;
        } else if (updateType == UpdateType.REMOVE) {
            newGames = getGames(player) - games;
        }
        this.bingo.getDatabaseLib().executeUpdateAsync("UPDATE " + GameData.getDatabaseTableName() + " SET games = ? WHERE uuid = ?", resultSet -> {
        }, newGames, player.getUniqueId().toString());
    }

    public void updateBrownSheeps(Player player, UpdateType updateType, int sheeps) {
        if (!GameData.getStats()) return;
        int newSheeps = 0;
        if (updateType == UpdateType.ADD) {
            newSheeps = getBrownSheeps(player) + sheeps;
        } else if (updateType == UpdateType.REMOVE) {
            newSheeps = getBrownSheeps(player) - sheeps;
        }
        this.bingo.getDatabaseLib().executeUpdateAsync("UPDATE " + GameData.getDatabaseTableName() + " SET brownSheeps = ? WHERE uuid = ?", resultSet -> {
        }, newSheeps, player.getUniqueId().toString());
    }

    public String getWGRatio(Player player) {
        double kd = (double) getWins(player) / (double) getGames(player);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(kd);
    }


    public enum UpdateType {
        ADD, REMOVE;
    }
}
