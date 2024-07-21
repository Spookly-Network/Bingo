package de.spookly.bingo.manager;

import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.GameData;
import org.bukkit.*;

public class WorldManager {

    private SpooklyBingoPlugin spooklyBingoPlugin;
    public WorldManager(SpooklyBingoPlugin spooklyBingoPlugin) {
        this.spooklyBingoPlugin = spooklyBingoPlugin;
    }

    public void setWorldSettingsForGameWorlds(World world) {
        Bukkit.getScheduler().runTask(spooklyBingoPlugin, () -> {
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
