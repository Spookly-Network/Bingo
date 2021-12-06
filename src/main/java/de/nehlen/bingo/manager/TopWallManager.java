package de.nehlen.bingo.manager;

import de.nehlen.bingo.Bingo;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.Rotatable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class TopWallManager {

    private final Bingo bingo;

    public TopWallManager(Bingo bingo) {
        this.bingo = bingo;
    }

    public void setWall() {
        Bukkit.getConsoleSender().sendMessage("Init -> setWall");

//        HashMap<Integer, String> rang = new HashMap<>();
        ArrayList<Location> locations = new ArrayList<>();

        this.bingo.getDatabaseLib().executeQueryAsync("SELECT * FROM spooklyProd.zayon_bingo_stats ORDER BY wins DESC LIMIT 10", resultSet -> {
//            UtilFunctions.debug(resultSet);
//
//
//            System.out.println(resultSet);
//
//            int in = 0;
//            try {
//                System.out.println(resultSet.getFetchSize());
//                System.out.println(resultSet.getFetchDirection());
//
//
//                while (resultSet.next()) {
//                    rang.put(in, resultSet.getString(in));
//                    in++;
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }

            locations.add(new Location(Bukkit.getWorld("WLobby"), 15, 47, -57));
            locations.add(new Location(Bukkit.getWorld("WLobby"), 15, 47, -56));
            locations.add(new Location(Bukkit.getWorld("WLobby"), 15, 47, -55));
            locations.add(new Location(Bukkit.getWorld("WLobby"), 15, 47, -54));
            locations.add(new Location(Bukkit.getWorld("WLobby"), 15, 47, -53));
            locations.add(new Location(Bukkit.getWorld("WLobby"), 15, 45, -57));
            locations.add(new Location(Bukkit.getWorld("WLobby"), 15, 45, -56));
            locations.add(new Location(Bukkit.getWorld("WLobby"), 15, 45, -55));
            locations.add(new Location(Bukkit.getWorld("WLobby"), 15, 45, -54));
            locations.add(new Location(Bukkit.getWorld("WLobby"), 15, 45, -53));

            Bukkit.getConsoleSender().sendMessage("Locations -> set");

            int i = 0;
            try {
                while (resultSet.next()) {
                    Bukkit.getConsoleSender().sendMessage("Init -> setWall Loop");

                    UUID uuid = UUID.fromString(String.valueOf(resultSet.getString("uuid")));
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                    int wins = resultSet.getInt("wins");
                    int games = resultSet.getInt("games");
                    Bukkit.getConsoleSender().sendMessage("Vars -> set");

                    int finalI = i;
                    Bukkit.getScheduler().runTask(this.bingo, () -> {
                        locations.get(finalI).add(0, -1, 0).getBlock().setType(Material.OAK_SIGN);
                        Bukkit.getConsoleSender().sendMessage("Sign -> set");

                        Sign sign = (Sign) locations.get(finalI).add(0, -1, 0).getBlock().getState();
                        Rotatable rotatableSign = (Rotatable) sign.getBlockData();
                        rotatableSign.setRotation(BlockFace.EAST);

                        sign.setBlockData(rotatableSign);
                        sign.setLine(0, "#" + finalI + 1);
                        sign.setLine(1, offlinePlayer.getName());
                        sign.setLine(2, "Wins: " + wins);
                        sign.setLine(3, "Ratio: " + wins + "/" + games);
                        sign.update();
                        Bukkit.getConsoleSender().sendMessage("Sign -> Update");

                    });

                    Bukkit.getConsoleSender().sendMessage("Sign -> set");

                    Bukkit.getScheduler().runTask(this.bingo, () -> {
                        locations.get(finalI).getBlock().setType(Material.PLAYER_HEAD);
                        Bukkit.getConsoleSender().sendMessage("Skull -> set");

                        Block skullBlock = locations.get(finalI).getBlock();
                        BlockState skullBlockState = skullBlock.getState();

                        ((Skull) skullBlockState).setOwningPlayer(offlinePlayer);
                        Rotatable rotatableSkull = (Rotatable) skullBlock.getBlockData();
                        rotatableSkull.setRotation(BlockFace.EAST);
                        skullBlock.setBlockData(rotatableSkull);
                        skullBlockState.update(true);
                        Bukkit.getConsoleSender().sendMessage("Skull -> update");
                    });
                    Bukkit.getConsoleSender().sendMessage("RUN -> " + i);
                    i++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
    }
}
