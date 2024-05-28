package de.nehlen.bingo.manager;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import org.bukkit.*;

public class WorldManager {

    private Bingo bingo;
    public WorldManager(Bingo bingo) {
        this.bingo = bingo;
    }

    public void setWorldSettingsForGameWorlds(World world) {
        Bukkit.getScheduler().runTask(bingo, () -> {
            world.setDifficulty(Difficulty.EASY);

            world.getWorldBorder().setCenter(new Location(world, 0, 0, 0));
            world.getWorldBorder().setSize(GameData.getWorldSize());

            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, true);
            world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);

            world.setTime(0);
        });
    }

    public void setWorldSettingsForLobbyWorlds(World world) {
        world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setDifficulty(Difficulty.EASY);
    }
}
