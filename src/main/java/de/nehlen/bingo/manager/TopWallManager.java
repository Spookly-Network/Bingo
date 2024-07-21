package de.nehlen.bingo.manager;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.mongodb.reactivestreams.client.MongoCollection;
import de.nehlen.bingo.Bingo;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.player.SpooklyOfflinePlayer;
import net.kyori.adventure.text.Component;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Sorts.descending;

public class TopWallManager {

    private final Bingo bingo;
    private List<Document> result = new ArrayList<>();

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

        MongoCollection<Document> collection = Spookly.getServer().getConnection().getCollection("spookly_player");
        //TODO change to right query
        collection.find(descending("_id"))
                .limit(10)
                .subscribe(new Subscriber<Document>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Document document) {
                        result.add(document);
                    }

                    @Override
                    public void onError(Throwable throwable) {}

                    @Override
                    public void onComplete() {
//                        startSetup();
                    }
                });

//        this.bingo.getDatabaseLib().executeQueryAsync("SELECT * FROM " + GameData.getDatabaseTableName() + " ORDER BY wins DESC LIMIT 10", resultSet -> {
//            try {
//                int i = 0;
//                while (resultSet.next()) {
//                    processResult(resultSet.getString("uuid"),
//                            resultSet.getInt("wins"),
//                            resultSet.getInt("games"),
//                            i,
//                            locations.get(i));
//                    i++;
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            }
//        });
    }

    private void startSetup() {
        int i = 0;
        do {
            Document document = result.get(i);
            //TODO
//            processResult(document.getString("uuid"),
//                    document.getString(GameData.getDatabaseTableName() + ".")
//                    );
            i++;
        } while (i < result.size());
    }

    private void processResult(String strUUID, Integer wins, Integer games, Integer place, Location location) {
        UUID uuid = UUID.fromString(strUUID);
        PlayerProfile playerProfile = Bukkit.createProfile(uuid);
        Spookly.getOfflinePlayer(uuid, offlinePlayer -> {
            playerProfile.getProperties().add(new ProfileProperty("textures", offlinePlayer.textureUrl()));
            updateStatsDisplay(location, playerProfile, offlinePlayer.name(), place, wins, games);
        });
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