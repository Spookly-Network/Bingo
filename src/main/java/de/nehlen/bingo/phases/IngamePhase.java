package de.nehlen.bingo.phases;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import de.spookly.Spookly;
import de.spookly.placeholder.Placeholder;
import de.spookly.placeholder.PlaceholderContext;
import de.spookly.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.bossbar.BossBarManager;
import de.nehlen.bingo.bossbar.BossBarSection;
import de.nehlen.bingo.bossbar.BossComponentHelper;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.bingo.statistics.player.BingoPlayerStats;
import de.nehlen.bingo.util.AbstractGamePhase;
import de.nehlen.bingo.util.UtilFunctions;
import de.nehlen.spooklycloudnetutils.helper.CloudStateHelper;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class IngamePhase extends AbstractGamePhase {
    private final Bingo bingo;
    private BossBar bossBar;
    private BossBarManager bossBarManager;

    public IngamePhase(Bingo bingo) {
        super(GameData.getMaxGameTime());
        this.bingo = bingo;

        bossBar = BossBar.bossBar(Component.empty(), 0, BossBar.Color.WHITE, BossBar.Overlay.NOTCHED_20);
        bossBarManager = new BossBarManager();
    }

    public void startPhase() {
        GameState.state = GameState.INGAME;

        CloudStateHelper.changeServiceToIngame();
        bingo.getWorldManager().setWorldSettingsForGameWorlds(Objects.requireNonNull(Bukkit.getWorld("world")));
        if (GameData.getActiveNether())
            bingo.getWorldManager().setWorldSettingsForGameWorlds(Objects.requireNonNull(Bukkit.getWorld("world_nether")));

        //TODO WIP begin
        Spookly.getPlaceholderManager().registerPlaceholder(new Placeholder("%time%",
                placeholderContext -> Component.text(UtilFunctions.formatTime(getCounter())),
                PlaceholderContext.PlaceholderType.BOSSBAR));
        Spookly.getPlaceholderManager().registerPlaceholder(new Placeholder("%team%",
                placeholderContext -> {
                    Optional<Team> optionalTeam = Optional.ofNullable(GameData.getTeamCache().get(placeholderContext.getPlayer()));
                    if (optionalTeam.isEmpty()) {
                        return Component.text("Kein Team");
                    }
                    Team team = optionalTeam.get();
                    return team.teamName();
                },
                PlaceholderContext.PlaceholderType.BOSSBAR));
        Spookly.getPlaceholderManager().registerPlaceholder(new Placeholder("%items%",
                placeholderContext -> {
                    Optional<Team> optionalTeam = Optional.ofNullable(GameData.getTeamCache().get(placeholderContext.getPlayer()));
                    if (optionalTeam.isEmpty()) {
                        return Component.text("0/9 Items");
                    }
                    PickList pickList = (PickList) optionalTeam.get().memory().get("picklist");
                    return Component.text(pickList.getAmountCompleted() + "/" + GameData.getItemsAmount());
                },
                PlaceholderContext.PlaceholderType.BOSSBAR));
        Spookly.getPlaceholderManager().registerPlaceholder(new Placeholder("%ticon%",
                placeholderContext -> {
                    Optional<Team> optionalTeam = Optional.ofNullable(GameData.getTeamCache().get(placeholderContext.getPlayer()));
                    if (optionalTeam.isEmpty()) {
                        return Component.text("\uE113")
                                .font(Key.key("hud"));
                    }
                    return optionalTeam.get().getTeamDisplay().getIcon();
                },
                PlaceholderContext.PlaceholderType.BOSSBAR));
        BossBarSection timeSection = new BossBarSection(BossComponentHelper.BossBackgroundSize.SIZE_64, '\uE100', Component.text("%time%"));
        BossBarSection teamSection = new BossBarSection(BossComponentHelper.BossBackgroundSize.SIZE_64, "%ticon%", Component.text("%team%"));
        BossBarSection itemsSection = new BossBarSection(BossComponentHelper.BossBackgroundSize.SIZE_64, '\uE102', Component.text("%items%"));

        bossBarManager.addSection(teamSection);
        bossBarManager.addSection(timeSection);
        bossBarManager.addSection(itemsSection);
        //TODO WIP end

        Spookly.getOnlinePlayers().forEach(splayer -> {
            Player player = splayer.toPlayer();
            BingoPlayerStats stats = bingo.getPlayerStatisticsManager().getPlayerStatistics(splayer);

            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.setTotalExperience(0);
            player.getActivePotionEffects().clear();

            stats.setGamesPlayed(stats.getGamesPlayed() + 1);
            Bingo.getBingo().getScoreboardManager().setUserScoreboard(player);

            //TODO WIP
            bossBarManager.showUserBossbar(player);
            //TODO WIP end
        });


        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bingo.getBingo(), () -> {

            //TODO WIP begin
            bossBarManager.tick();
//            bossBar.name(
//                    Component.empty()
//                            .append(BossComponentHelper.container(BossComponentHelper.BossBackgroundSize.SIZE_64,
//                                    Component.empty()
//                                            .append(Component.text('\uE100').font(Key.key("hud")).color(BossComponentHelper.getNoShadowColor()))
//                                            .append(Component.text('\uEFE2').font(Key.key("hud")))
//                                            .append(Component.text(Bingo.getBingo().getScoreboardManager().getTeam(player)))))
//                            .append(Component.text('\uEFE6').font(Key.key("hud"))) //64 spacer
//                            .append(BossComponentHelper.container(BossComponentHelper.BossBackgroundSize.SIZE_64,
//                                    Component.empty() //Time
//                                            .append(Component.text('\uE100').font(Key.key("hud")).color(BossComponentHelper.getNoShadowColor()))
//                                            .append(Component.text('\uEFE2').font(Key.key("hud")))
//                                            .append(Component.text(UtilFunctions.formatTime(getCounter())))))
//            );

            /*
            Bukkit.getScheduler().runTaskAsynchronously(bingo, () -> {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.sendActionBar(Component.empty()
                            .append(Bingo.getBingo().getScoreboardManager().getTeam(player))
                            .append(TextComponentHelper.seperator())
                            .append(Component.text(UtilFunctions.formatTime(getCounter())).color(NamedTextColor.WHITE)));
                });
            });*/
            //TODO WIP end


            if (counter == 0) {
                Map<Team, Integer> teamItems = new HashMap<>();
                Spookly.getTeamManager().registeredTeams().forEach(team -> {
                    teamItems.put(team, 9 - ((PickList) team.memory().get("picklist")).getAmountCompleted());

                });

                Team winner = teamItems.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toList())
                        .get(0).getKey();
                bingo.getEndingPhase().teamWin(winner);
                Bukkit.broadcast(StringData.getPrefix()
                        .append(Component.text("Die Spielzeit ist abgelaufen, das Team mit den meißten Items hat gewonnen.").color(NamedTextColor.GRAY)));
            }
            counter--;
        }, 20L, 20L);
    }
}
