package de.nehlen.bingo;

import de.nehlen.bingo.commands.*;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.bingo.factory.UserFactory;
import de.nehlen.bingo.inventroy.TeamSelectInventroy;
import de.nehlen.bingo.listener.*;
import de.nehlen.bingo.manager.ScoreboardManager;
import de.nehlen.bingo.manager.TopWallManager;
import de.nehlen.bingo.manager.WorldManager;
import de.nehlen.bingo.phases.EndingCoutdown;
import de.nehlen.bingo.phases.IngameCountdown;
import de.nehlen.bingo.phases.LobbyCountdown;
import de.nehlen.bingo.phases.TeleportPhase;
import de.nehlen.bingo.sidebar.SidebarCache;
import de.nehlen.gameapi.Gameapi;
import de.nehlen.gameapi.TeamAPI.BasicTeam;
import de.nehlen.gameapi.TeamAPI.Team;
import de.nehlen.gameapi.YamlConfiguration.SpigotConfigurationWrapper;
import de.nehlen.gameapi.YamlConfiguration.YamlConfig;
import de.nehlen.gameapi.util.DatabaseLib;
import de.nehlen.spooklycloudnetutils.helper.CloudStateHelper;
import de.nehlen.spooklycloudnetutils.helper.CloudWrapperHelper;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import org.ipvp.canvas.MenuFunctionListener;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class Bingo extends JavaPlugin {

    @Getter private static Bingo bingo;
    @Getter private SpigotConfigurationWrapper generalConfig;
    @Getter private SpigotConfigurationWrapper locationConfig;
    @Getter private SpigotConfigurationWrapper itemsConfig;
    @Getter private SidebarCache sidebarCache;
    @Getter private ScoreboardManager scoreboardManager;
    @Getter private DatabaseLib databaseLib;
    @Getter private UserFactory userFactory;

    @Getter private WorldManager worldManager;
    @Getter private TopWallManager topWallManager;

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
    @Getter private EntityDeathListener entityDeathListener;
    @Getter private BuildListener buildListener;
    @Getter private TeamListener teamListener;
    @Getter private ItemCheckListener itemCheckListener;
    @Getter private TeamSelectInventroy teamSelectInventroy;

    @Getter private BingoCommand bingoCommand;
    @Getter private StartCommand startCommand;
    @Getter private BackpackCommand backpackCommand;
    @Getter private SetspawnCommand setspawnCommand;
    @Getter private StatsCommand statsCommand;

    @Getter private LobbyCountdown lobbyCountdown;
    @Getter private TeleportPhase teleportPhase;
    @Getter private IngameCountdown ingameCountdown;
    @Getter private EndingCoutdown endingCoutdown;


    @Override
    public void onEnable() {
        bingo = this;
        this.generalConfig = new YamlConfig(new File(getDataFolder(), "general_settings.yml"));
        this.locationConfig = new YamlConfig(new File(getDataFolder(), "location_settings.yml"));
        this.itemsConfig = new YamlConfig(new File(getDataFolder(), "items_settings.yml"));

        this.sidebarCache = new SidebarCache();
        this.scoreboardManager = new ScoreboardManager(this);
        this.databaseLib = Gameapi.getGameapi().getDatabaseLib();
        this.userFactory = new UserFactory(this);
        this.topWallManager = new TopWallManager(this);
        this.worldManager = new WorldManager(this);

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
        this.entityDeathListener = new EntityDeathListener(this);
        this.weatherChangeListener = new WeatherChangeListener(this);
        this.buildListener = new BuildListener(this);
        this.itemCheckListener = new ItemCheckListener(this);
        this.teamListener = new TeamListener();
        this.teamSelectInventroy = new TeamSelectInventroy(this);

        this.lobbyCountdown = new LobbyCountdown(this);
        this.teleportPhase = new TeleportPhase(this);
        this.ingameCountdown = new IngameCountdown(this);
        this.endingCoutdown = new EndingCoutdown(this);

        this.bingoCommand = new BingoCommand(this);
        this.startCommand = new StartCommand(this);
        this.backpackCommand = new BackpackCommand(this);
        this.setspawnCommand = new SetspawnCommand(this);
        this.statsCommand = new StatsCommand(this);

        WorldCreator w = WorldCreator.name("lobby_bingo");
        Bukkit.createWorld(w);
        bingo.getServer().getWorlds().add(Bukkit.getWorld("lobby_bingo"));

        this.userFactory.createTable();
        // SET BINGO ITEMS IN GAMEDATA
        LobbyCountdown.fillItemList();
        loadTeams();

        Bukkit.getPluginManager().registerEvents(new MenuFunctionListener(), this);
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
        Bukkit.getPluginManager().registerEvents(this.entityDeathListener, this);
        Bukkit.getPluginManager().registerEvents(this.itemCheckListener, this);
        Bukkit.getPluginManager().registerEvents(this.teamSelectInventroy, this);
        Bukkit.getPluginManager().registerEvents(this.teamListener, this);
        getCommand("bingo").setExecutor(this.bingoCommand);
        getCommand("start").setExecutor(this.startCommand);
        getCommand("setspawn").setExecutor(this.setspawnCommand);
        getCommand("backpack").setExecutor(this.backpackCommand);
        getCommand("stats").setExecutor(this.statsCommand);
        this.worldManager.setWorldSettingsForLobbyWorlds(Objects.requireNonNull(Bukkit.getWorld("Lobby_Bingo")));
        this.getTopWallManager().setWall();

        CloudStateHelper.changeServiceMotd(GameData.getTeamAmount() + "x" + GameData.getTeamSize());
        CloudStateHelper.changeServiceMaxPlayers(GameData.getTeamAmount() * GameData.getTeamSize());
        CloudWrapperHelper.publishServiceInfoUpdate();
    }

    @Override
    public void onDisable() {

    }

    public static void loadTeams() {
        List<TextColor> colors = List.of(
                        NamedTextColor.RED,
                        NamedTextColor.BLUE,
                        NamedTextColor.GREEN,
                        NamedTextColor.YELLOW,
                        NamedTextColor.LIGHT_PURPLE,
                        NamedTextColor.AQUA,
                        NamedTextColor.GOLD,
                        NamedTextColor.DARK_AQUA,
                        NamedTextColor.DARK_RED,
                        NamedTextColor.DARK_BLUE,
                        NamedTextColor.DARK_PURPLE);


        for (int i = 0; i < GameData.getTeamAmount(); i++) {

            Team team = new BasicTeam();
            PickList pickList = new PickList(GameData.getItemsToFind());
            team.addToMemory("picklist", pickList);

            team.maxTeamSize(GameData.getTeamSize());
            team.teamColor(colors.get(i));
            team.teamName(Component.text("Team-" + (i + 1)).color(colors.get(i)));
            Gameapi.getGameapi().getTeamAPI().addTeam(team);

        }

    }
}
