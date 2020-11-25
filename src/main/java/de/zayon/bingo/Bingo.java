package de.zayon.bingo;

import de.exceptionflug.mccommons.config.shared.ConfigFactory;
import de.exceptionflug.mccommons.config.shared.ConfigWrapper;
import de.exceptionflug.mccommons.config.spigot.SpigotConfig;
import de.zayon.bingo.commands.BingoCommand;
import de.zayon.bingo.commands.StartCommand;
import de.zayon.bingo.countdowns.EndingCoutdown;
import de.zayon.bingo.countdowns.IngameCountdown;
import de.zayon.bingo.countdowns.LobbyCountdown;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.TeamData;
import de.zayon.bingo.data.helper.Team;
import de.zayon.bingo.factory.UserFactory;
import de.zayon.bingo.inventroy.BasicInventory;
import de.zayon.bingo.listener.*;
import de.zayon.bingo.manager.GroupManager;
import de.zayon.bingo.manager.ScoreboardManager;
import de.zayon.bingo.sidebar.SidebarCache;
import de.zayon.bingo.util.DatabaseLib;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Bingo extends JavaPlugin {

    @Getter private static Bingo bingo;
    @Getter private SpigotConfig generalConfig;
    @Getter private SpigotConfig locationConfig;
    @Getter private SidebarCache sidebarCache;
    @Getter private ScoreboardManager scoreboardManager;
    @Getter private GroupManager groupManager;
    @Getter private DatabaseLib databaseLib;
    @Getter private UserFactory userFactory;

    @Getter private AsyncPlayerChatListener asyncPlayerChatListener;
    @Getter private DamageListener damageListener;
    @Getter private FoodLevelChangeListener foodLevelChangeListener;
    @Getter private PlayerDeathListener playerDeathListener;
    @Getter private PlayerDropItemListener playerDropItemListener;
    @Getter private PlayerInteractListener playerInteractListener;
    @Getter private PlayerJoinListener playerJoinListener;
    @Getter private PlayerLoginListener playerLoginListener;
    @Getter private PlayerQuitListener playerQuitListener;
    @Getter private ServerPingListener serverPingListener;
    @Getter private WeatherChangeListener weatherChangeListener;
    @Getter private BasicInventory basicInventory;
    @Getter private BuildListener buildListener;

    @Getter private BingoCommand bingoCommand;
    @Getter private StartCommand startCommand;

    @Getter private EndingCoutdown endingCoutdown;
    @Getter private IngameCountdown ingameCountdown;
    @Getter private LobbyCountdown lobbyCountdown;


    @Override
    public void onEnable() {
        bingo = this;

        this.generalConfig = ConfigFactory.create(new File(getDataFolder(), "general_settings.yml"), SpigotConfig.class);
        this.locationConfig = ConfigFactory.create(new File(getDataFolder(), "location_settings.yml"), SpigotConfig.class);

        this.sidebarCache = new SidebarCache();
        this.scoreboardManager = new ScoreboardManager(this);
        this.groupManager = new GroupManager();
        this.databaseLib = new DatabaseLib(this);
        this.userFactory = new UserFactory(this);

        this.asyncPlayerChatListener = new AsyncPlayerChatListener(this);
        this.damageListener = new DamageListener(this);
        this.foodLevelChangeListener = new FoodLevelChangeListener(this);
        this.playerDeathListener = new PlayerDeathListener(this);
        this.playerDropItemListener = new PlayerDropItemListener(this);
        this.playerInteractListener = new PlayerInteractListener(this);
        this.playerJoinListener = new PlayerJoinListener(this);
        this.playerLoginListener = new PlayerLoginListener(this);
        this.playerQuitListener = new PlayerQuitListener(this);
        this.serverPingListener = new ServerPingListener(this);
        this.weatherChangeListener = new WeatherChangeListener(this);
        this.buildListener = new BuildListener(this);
        this.basicInventory = new BasicInventory(this);

        this.bingoCommand = new BingoCommand(this);
        this.startCommand = new StartCommand(this);

        WorldCreator w = WorldCreator.name("WLobby");
        Bukkit.createWorld(w);
        bingo.getServer().getWorlds().add(Bukkit.getWorld("WLobby"));

        this.userFactory.createTable();
        loadTeams();

        Bukkit.getPluginManager().registerEvents(this.asyncPlayerChatListener, this);
        Bukkit.getPluginManager().registerEvents(this.damageListener, this);
        Bukkit.getPluginManager().registerEvents(this.foodLevelChangeListener, this);
        Bukkit.getPluginManager().registerEvents(this.playerDeathListener, this);
        Bukkit.getPluginManager().registerEvents(this.playerDropItemListener, this);
        Bukkit.getPluginManager().registerEvents(this.playerInteractListener, this);
        Bukkit.getPluginManager().registerEvents(this.playerJoinListener, this);
        Bukkit.getPluginManager().registerEvents(this.playerLoginListener, this);
        Bukkit.getPluginManager().registerEvents(this.playerQuitListener, this);
        Bukkit.getPluginManager().registerEvents(this.serverPingListener, this);
        Bukkit.getPluginManager().registerEvents(this.weatherChangeListener, this);
        Bukkit.getPluginManager().registerEvents(this.buildListener, this);
        Bukkit.getPluginManager().registerEvents(this.basicInventory, this);
        getCommand("bingo").setExecutor(this.bingoCommand);
        getCommand("start").setExecutor(this.startCommand);

        Bukkit.getWorld("world").setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        Bukkit.getWorld("WLobby").setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        Bukkit.getWorld("world").setTime(5000L);
        // SET BINGO ITEMS IN GAMEDATA
        this.getLobbyCountdown().fillItemList();

    }

    public static void loadTeams() {
        for (int i = 0; i < GameData.getTeamAmount(); i++) {
            Team t = new Team();
            t.setTeamID(i);
            t.setMaxSize(GameData.getTeamSize());
            TeamData.teamCache.add(t);
        }
    }
}
