package de.nehlen.bingo;

import de.nehlen.bingo.commands.*;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.listener.*;
import de.nehlen.bingo.manager.ScoreboardManager;
import de.nehlen.bingo.manager.TopWallManager;
import de.nehlen.bingo.manager.WorldManager;
import de.nehlen.bingo.phases.EndingPhase;
import de.nehlen.bingo.phases.IngamePhase;
import de.nehlen.bingo.phases.LobbyPhase;
import de.nehlen.bingo.phases.TeleportPhase;
import de.nehlen.bingo.statistics.player.PlayerStatisticsManager;
import de.spookly.Spookly;
import de.spookly.configuration.ConfigurationWrapper;
import de.spookly.plugin.SpooklyPlugin;
import de.nehlen.spooklycloudnetutils.helper.CloudStateHelper;
import de.nehlen.spooklycloudnetutils.helper.CloudWrapperHelper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import java.io.File;
import java.util.Objects;

public class Bingo extends SpooklyPlugin {

    @Getter private static Bingo bingo;
    @Getter private static BingoRegistry registry;

    @Getter private ConfigurationWrapper generalConfig;
    @Getter private ConfigurationWrapper locationConfig;
    @Getter private ConfigurationWrapper itemsConfig;
    @Getter private ScoreboardManager scoreboardManager;

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
    @Getter private IngamePhase ingamePhase;
    @Getter private EndingPhase endingPhase;

    @Getter private PlayerStatisticsManager playerStatisticsManager;

    @Override
    public void load() {
        bingo = this;
    }

    @Override
    public void enable() {
        registry = new BingoRegistry(this);
        this.generalConfig = Spookly.getServer().createConfiguration(new File(getDataFolder(), "general_settings.yml"));
        this.locationConfig = Spookly.getServer().createConfiguration(new File(getDataFolder(), "location_settings.yml"));
        this.itemsConfig = Spookly.getServer().createConfiguration(new File(getDataFolder(), "items_settings.yml"));

        this.scoreboardManager = new ScoreboardManager(this);
        this.topWallManager = new TopWallManager(this);
        this.worldManager = new WorldManager(this);
        this.playerStatisticsManager = new PlayerStatisticsManager();

        this.asyncPlayerChatListener = new AsyncPlayerChatListener();
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
        this.ingamePhase = new IngamePhase(this);
        this.endingPhase = new EndingPhase(this);

        this.bingoCommand = new BingoCommand(this);
        this.startCommand = new StartCommand(this);
        this.backpackCommand = new BackpackCommand(this);
        this.setspawnCommand = new SetspawnCommand(this);
        this.statsCommand = new StatsCommand(this);
        this.rerollCommand = new RerollCommand();

        WorldCreator w = WorldCreator.name("lobby_bingo");
        Bukkit.createWorld(w);
        bingo.getServer().getWorlds().add(Bukkit.getWorld("lobby_bingo"));

        // SET BINGO ITEMS IN GAMEDATA
        LobbyPhase.fillItemList();

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
        this.worldManager.setWorldSettingsForLobbyWorlds(Objects.requireNonNull(Bukkit.getWorld("Lobby_Bingo")));
    }

    @Override
    protected void disable() {

    }

    @Override
    protected void postStartup() {
        this.getTopWallManager().setWall();

        registry.registerTranslations();
        registry.registerTeams();

        CloudStateHelper.changeServiceMotd(GameData.getTeamAmount() + "x" + GameData.getTeamSize());
        CloudStateHelper.changeServiceMaxPlayers(GameData.getTeamAmount() * GameData.getTeamSize());
        CloudWrapperHelper.publishServiceInfoUpdate();
    }
}
