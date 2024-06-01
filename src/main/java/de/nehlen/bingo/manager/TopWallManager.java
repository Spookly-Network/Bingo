package de.nehlen.bingo.manager;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.player.SpooklyOfflinePlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class TopWallManager {

    private final Bingo bingo;

    public TopWallManager(Bingo bingo) {
        this.bingo = bingo;
    }

    public void setWall() {
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(new Location(Bukkit.getWorld("lobby_bingo"), 15, 47, -57));
        locations.add(new Location(Bukkit.getWorld("lobby_bingo"), 15, 47, -56));
        locations.add(new Location(Bukkit.getWorld("lobby_bingo"), 15, 47, -55));
        locations.add(new Location(Bukkit.getWorld("lobby_bingo"), 15, 47, -54));
        locations.add(new Location(Bukkit.getWorld("lobby_bingo"), 15, 47, -53));
        locations.add(new Location(Bukkit.getWorld("lobby_bingo"), 15, 45, -57));
        locations.add(new Location(Bukkit.getWorld("lobby_bingo"), 15, 45, -56));
        locations.add(new Location(Bukkit.getWorld("lobby_bingo"), 15, 45, -55));
        locations.add(new Location(Bukkit.getWorld("lobby_bingo"), 15, 45, -54));
        locations.add(new Location(Bukkit.getWorld("lobby_bingo"), 15, 45, -53));

        this.bingo.getDatabaseLib().executeQueryAsync("SELECT * FROM " + GameData.getDatabaseTableName() + " ORDER BY wins DESC LIMIT 10", resultSet -> {
            try {
                int i = 0;
                while (resultSet.next()) {
                    processResult(resultSet.getString("uuid"),
                            resultSet.getInt("wins"),
                            resultSet.getInt("games"),
                            i,
                            locations.get(i));
                    i++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    private void processResult(String strUUID, Integer wins, Integer games, Integer place, Location location) {
        UUID uuid = UUID.fromString(strUUID);
        PlayerProfile playerProfile = Bukkit.createProfile(uuid);
        SpooklyOfflinePlayer offlinePlayer = Spookly.getOfflinePlayer(uuid);

        playerProfile.getProperties().add(new ProfileProperty("textures", offlinePlayer.textureUrl()));
        updateStatsDisplay(location, playerProfile, offlinePlayer.name(), place, wins, games);
    }

    private void updateStatsDisplay(Location location, PlayerProfile playerProfile, String playerName, Integer place, Integer wins, Integer games) {
        Bukkit.getScheduler().runTask(Bingo.getBingo(), () -> {

            Block skullBlock = location.getBlock();
            Skull skull = (Skull) skullBlock.getState();
            skull.setPlayerProfile(playerProfile);
            skull.update();

            Sign sign = (Sign) location.add(0, -1, 0).getBlock().getState();
            SignSide side = sign.getSide(Side.FRONT);
            side.line(0, Component.text("#" + (place + 1)));
            side.line(1, Component.text(playerName));
            side.line(2, Component.text("Wins: " + wins));
            side.line(3, Component.text("Ratio: " + wins + "/" + games));
            sign.setWaxed(true);
            sign.update();
        });
    }
}