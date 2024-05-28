package de.nehlen.bingo;

import de.nehlen.bingo.commands.*;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.bingo.factory.UserFactory;
import de.nehlen.bingo.listener.*;
import de.nehlen.bingo.manager.ScoreboardManager;
import de.nehlen.bingo.manager.TopWallManager;
import de.nehlen.bingo.manager.WorldManager;
import de.nehlen.bingo.phases.EndingCoutdown;
import de.nehlen.bingo.phases.IngameCountdown;
import de.nehlen.bingo.phases.LobbyPhase;
import de.nehlen.bingo.phases.TeleportPhase;
import de.nehlen.bingo.sidebar.SidebarCache;
import de.nehlen.bingo.util.fonts.TeamFont;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.configuration.ConfigurationWrapper;
import de.nehlen.spookly.database.Connection;
import de.nehlen.spookly.plugin.SpooklyPlugin;
import de.nehlen.spooklycloudnetutils.helper.CloudStateHelper;
import de.nehlen.spooklycloudnetutils.helper.CloudWrapperHelper;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.ipvp.canvas.MenuFunctionListener;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class Bingo extends SpooklyPlugin {

    @Getter private static Bingo bingo;
    @Getter private BingoRegistry bingoRegistry;
    @Getter private ConfigurationWrapper generalConfig;
    @Getter private ConfigurationWrapper locationConfig;
    @Getter private ConfigurationWrapper itemsConfig;
    @Getter private ScoreboardManager scoreboardManager;
    @Getter private Connection databaseLib;
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

    @Getter private BingoCommand bingoCommand;
    @Getter private StartCommand startCommand;
    @Getter private BackpackCommand backpackCommand;
    @Getter private SetspawnCommand setspawnCommand;
    @Getter private StatsCommand statsCommand;
    @Getter private RerollCommand rerollCommand;

    @Getter private LobbyPhase lobbyPhase;
    @Getter private TeleportPhase teleportPhase;
    @Getter private IngameCountdown ingameCountdown;
    @Getter private EndingCoutdown endingCoutdown;

    @Override
    public void load() {
        bingo = this;
    }

    @Override
    public void enable() {
        this.bingoRegistry = new BingoRegistry(this);
        this.generalConfig = Spookly.getServer().createConfiguration(new File(getDataFolder(), "general_settings.yml"));
        this.locationConfig = Spookly.getServer().createConfiguration(new File(getDataFolder(), "location_settings.yml"));
        this.itemsConfig = Spookly.getServer().createConfiguration(new File(getDataFolder(), "items_settings.yml"));

        this.scoreboardManager = new ScoreboardManager(this);
        this.databaseLib = Spookly.getServer().getConnection();
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

        this.lobbyPhase = new LobbyPhase(this);
        this.teleportPhase = new TeleportPhase(this);
        this.ingameCountdown = new IngameCountdown(this);
        this.endingCoutdown = new EndingCoutdown(this);

        this.bingoCommand = new BingoCommand(this);
        this.startCommand = new StartCommand(this);
        this.backpackCommand = new BackpackCommand(this);
        this.setspawnCommand = new SetspawnCommand(this);
        this.statsCommand = new StatsCommand(this);
        this.rerollCommand = new RerollCommand();

        WorldCreator w = WorldCreator.name("lobby_bingo");
        Bukkit.createWorld(w);
        bingo.getServer().getWorlds().add(Bukkit.getWorld("lobby_bingo"));

        this.userFactory.createTable();
        // SET BINGO ITEMS IN GAMEDATA
        LobbyPhase.fillItemList();

        try {
            loadTeams();
        } catch (NoSuchFieldException e) {
            Bukkit.getLogger().log(Level.SEVERE, "could not load Team because team prefix char was not present");
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            Bukkit.getLogger().log(Level.SEVERE, "just no");
            throw new RuntimeException(e);
        }

        registerEvent(new MenuFunctionListener());
        registerEvent(this.asyncPlayerChatListener);
        registerEvent(this.damageListener);
        registerEvent(this.foodLevelChangeListener);
        registerEvent(this.playerDeathListener);
        registerEvent(this.playerDropItemListener);
        registerEvent(this.playerInteractListener);
        registerEvent(this.playerJoinListener);
        registerEvent(this.playerLoginListener);
        registerEvent(this.playerQuitListener);
        registerEvent(this.serverPingListener);
        registerEvent(this.weatherChangeListener);
        registerEvent(this.buildListener);
        registerEvent(this.entityDeathListener);
        registerEvent(this.itemCheckListener);
        registerEvent(this.teamListener);

        registerCommand("reroll", this.rerollCommand);
        registerCommandOnly("bingo", this.bingoCommand);
        registerCommandOnly("start", this.startCommand);
        registerCommandOnly("setspawn", this.setspawnCommand);
        registerCommandOnly("backpack", this.backpackCommand);
        registerCommandOnly("stats", this.statsCommand);
        registerCommandOnly("hud", new hudCommand());
//        registerCommandOnly("test", new TestCmd());
        this.worldManager.setWorldSettingsForLobbyWorlds(Objects.requireNonNull(Bukkit.getWorld("Lobby_Bingo")));

        postStartup();
    }

    @Override
    protected void disable() {

    }

    @Override
    protected void postStartup() {
        this.getTopWallManager().setWall();
        this.bingoRegistry.registerTranslatables();

        CloudStateHelper.changeServiceMotd(GameData.getTeamAmount() + "x" + GameData.getTeamSize());
        CloudStateHelper.changeServiceMaxPlayers(GameData.getTeamAmount() * GameData.getTeamSize());
        CloudWrapperHelper.publishServiceInfoUpdate();
    }


    public static void loadTeams() throws NoSuchFieldException, IllegalAccessException {
        List<TextColor> teamColor = List.of(NamedTextColor.RED, TextColor.fromHexString("#448400"), TextColor.fromHexString("#00aeff"), TextColor.fromHexString("#ffb100"), NamedTextColor.LIGHT_PURPLE, TextColor.fromHexString("#ff5733"), TextColor.fromHexString("#009688"), TextColor.fromHexString("#9c27b0"), TextColor.fromHexString("#c62828"));

        for (int i = 0; i < GameData.getTeamAmount(); i++) {
            Field field = TeamFont.class.getDeclaredField("TEAM_" + (i + 1));
            PickList picklist = new PickList(GameData.getItemsToFind());

            Spookly.getTeamManager().registerTeam(Spookly.buildTeam().teamColor(teamColor.get(i)).teamName(Component.text("Team-" + (i + 1)).color(teamColor.get(i))).maxTeamSize(GameData.getTeamSize()).prefix(Component.empty().append(Component.text((String) field.get(null)).font(TeamFont.KEY).color(NamedTextColor.WHITE)).append(Component.text(" "))).tabSortId((i + 1)).addToMemory("picklist", picklist).build());
        }
    }
}
