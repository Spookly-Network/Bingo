package de.nehlen.bingo.data;

import de.nehlen.bingo.Bingo;
import de.nehlen.spookly.team.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameData {

    @Getter private static Integer teamSize = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.game.teamSize", 2);
    @Getter private static Integer teamAmount = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.game.teamAmount", 9);
    @Getter private static Boolean isHunger = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.game.hungerEnabled", true);
    @Getter private static Boolean stats = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.game.stats", false);
    @Getter private static Integer startTime = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.game.startTime", 60);
    @Getter private static Integer maxGameTime = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.game.maxGameTime", 7200);
    @Getter private static Integer worldSize = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.game.worldSize", 13000);
    @Getter private static Integer playerSpawnSize = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.game.playerSpawnSize", 10000);
    @Getter private static Boolean activeNether = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.game.activeNether", true);
    @Getter private static Integer itemsAmount = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.game.itemsAmount", 9);
    @Getter private static String databaseTableName = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.database.tableName", "spookly_bingo_stats");
        @Getter private static Integer minPlayerToStartGame = Math.toIntExact(Math.round(teamSize * 1.5));
//    @Getter private static Integer minPlayerToStartGame = 2;
    public static boolean firstItemFound = false;

    public static Location getLobbyLocation() {
        World lobbyWorld = Bukkit.getWorld("lobby_bingo");
        return new Location(lobbyWorld, 4.5, 44, -71.5, 0, 0);
    }

    @Getter private static HashMap<Player, Team> teamCache = new HashMap<>();
    @Getter @Setter private static ArrayList<Material> itemsToFind = new ArrayList<Material>();
    @Getter @Setter private static ArrayList<Player> ingame = new ArrayList<Player>();
    @Getter private static List<String> itemPool = Bingo.getBingo().getItemsConfig().getOrSetDefault("config.itempool", new ArrayList<String>(
            Arrays.asList(
                    Material.ENCHANTING_TABLE.toString(),
                    Material.GOLDEN_APPLE.toString(),
                    Material.ANVIL.toString(),
                    Material.TNT.toString(),
                    Material.BELL.toString(),
                    Material.SWEET_BERRIES.toString(),
                    Material.CROSSBOW.toString(),
                    Material.PRISMARINE_SHARD.toString(),
                    Material.HEART_OF_THE_SEA.toString(),
                    Material.DIAMOND_LEGGINGS.toString(),
                    Material.DIAMOND_CHESTPLATE.toString(),
                    Material.POISONOUS_POTATO.toString(),
                    Material.FIREWORK_ROCKET.toString(),
                    Material.SALMON.toString(),
                    Material.ENDER_PEARL.toString(),
                    Material.FERMENTED_SPIDER_EYE.toString(),
                    Material.EMERALD.toString(),
                    Material.RABBIT_STEW.toString(),
                    Material.GLISTERING_MELON_SLICE.toString(),
                    Material.COMPOSTER.toString(),
                    Material.PISTON.toString(),
                    Material.REDSTONE_LAMP.toString(),
                    Material.DISPENSER.toString(),
                    Material.WRITABLE_BOOK.toString(),
                    Material.SHIELD.toString(),
                    Material.SCAFFOLDING.toString(),
                    Material.ARMOR_STAND.toString(),
                    Material.CACTUS.toString(),
                    Material.HAY_BLOCK.toString(),
                    Material.CAKE.toString(),
                    Material.PUMPKIN_PIE.toString(),
                    Material.SEA_PICKLE.toString(),
                    Material.NOTE_BLOCK.toString(),
                    Material.JUKEBOX.toString(),
                    Material.JACK_O_LANTERN.toString(),
                    Material.BOOKSHELF.toString(),
                    Material.DRIED_KELP.toString(),
                    Material.PUFFERFISH.toString(),
                    Material.CARROT_ON_A_STICK.toString(),
                    Material.BIRCH_BOAT.toString(),
                    Material.CAULDRON.toString(),
                    Material.CLOCK.toString()
            )
    ));

    @Getter private static List<String> noSpawnBiomes = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.game.noSpawnBiomes", new ArrayList<String>(
            Arrays.asList(
                    Biome.OCEAN.toString(),
                    Biome.COLD_OCEAN.toString(),
                    Biome.DEEP_COLD_OCEAN.toString(),
                    Biome.DEEP_FROZEN_OCEAN.toString(),
                    Biome.DEEP_LUKEWARM_OCEAN.toString(),
                    Biome.DEEP_OCEAN.toString(),
                    Biome.FROZEN_OCEAN.toString(),
                    Biome.WARM_OCEAN.toString(),
                    Biome.LUKEWARM_OCEAN.toString(),
                    Biome.RIVER.toString(),
                    Biome.FROZEN_RIVER.toString(),
                    Biome.BEACH.name(),
                    Biome.BADLANDS.name(),
                    Biome.ERODED_BADLANDS.name(),
                    Biome.WOODED_BADLANDS.name(),
                    Biome.DESERT.name()
            )
    ));

    @Getter private static String configVer = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.version", "1.2");

}
