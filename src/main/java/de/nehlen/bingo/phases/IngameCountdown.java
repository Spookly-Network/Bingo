package de.nehlen.bingo.phases;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.commands.hudCommand;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.bingo.data.helper.TextComponentHelper;
import de.nehlen.bingo.util.AbstractGamePhase;
import de.nehlen.bingo.util.UtilFunctions;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.team.Team;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class IngameCountdown extends AbstractGamePhase {
    private final Bingo bingo;
    private BossBar bossBar;
    private Map<Player, BossBar> bossBars = new HashMap<>();

    public IngameCountdown(Bingo bingo) {
        super(GameData.getMaxGameTime());
        this.bingo = bingo;

        bossBar = BossBar.bossBar(Component.empty(), 0, BossBar.Color.WHITE, BossBar.Overlay.NOTCHED_20);
    }

    public void startPhase() {
        GameState.state = GameState.INGAME;
        bingo.getWorldManager().setWorldSettingsForGameWorlds(Objects.requireNonNull(Bukkit.getWorld("world")));
        if (GameData.getActiveNether())
            bingo.getWorldManager().setWorldSettingsForGameWorlds(Objects.requireNonNull(Bukkit.getWorld("world_nether")));

        Bukkit.getOnlinePlayers().forEach(player -> {
            Bingo.getBingo().getScoreboardManager().setUserScoreboard(player);
            if (hudCommand.getBetaPlayer().contains(player))
                player.showBossBar(bossBar);
        });


        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bingo.getBingo(), () -> {
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


            Bukkit.getScheduler().runTaskAsynchronously(bingo, () -> {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.sendActionBar(Component.empty()
                            .append(Bingo.getBingo().getScoreboardManager().getTeam(player))
                            .append(TextComponentHelper.seperator())
                            .append(Component.text(UtilFunctions.formatTime(getCounter())).color(NamedTextColor.WHITE)));
                });
            });


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
