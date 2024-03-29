package de.nehlen.bingo.phases;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.bingo.data.helper.TextComponentHelper;
import de.nehlen.bingo.util.Items;
import de.nehlen.bingo.util.UtilFunctions;
import de.nehlen.gameapi.Gameapi;
import de.nehlen.gameapi.PhaseApi.AbstractGamePhase;
import de.nehlen.gameapi.TeamAPI.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class IngameCountdown extends AbstractGamePhase {
    private final Bingo bingo;

    public IngameCountdown(Bingo bingo) {
        super(GameData.getMaxGameTime());
        this.bingo = bingo;
    }

    public void startPhase() {
        GameState.state = GameState.INGAME;
        bingo.getWorldManager().setWorldSettingsForGameWorlds(Objects.requireNonNull(Bukkit.getWorld("world")));
        if (GameData.getActiveNether())
            bingo.getWorldManager().setWorldSettingsForGameWorlds(Objects.requireNonNull(Bukkit.getWorld("world_nether")));

        Bukkit.getOnlinePlayers().forEach(Bingo.getBingo().getScoreboardManager()::setUserScoreboard);

        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bingo.getBingo(), () -> {
            Bukkit.getScheduler().runTaskAsynchronously(bingo, () -> {
                Bukkit.getOnlinePlayers().forEach(player -> {

                    player.sendActionBar(Bingo.getBingo().getScoreboardManager().getTeam(player)
                            .append(TextComponentHelper.seperator())
                            .append(Component.text(UtilFunctions.formatTime(getCounter())).color(NamedTextColor.WHITE)));
                });
            });


            if (counter == 0) {
                Map<Team, Integer> teamItems = new HashMap<>();
                Gameapi.getGameapi().getTeamAPI().registeredTeams().forEach(team -> {
                    teamItems.put(team, 9 - ((PickList) team.memory().get("picklist")).getAmountCompleted());

                });

                Team winner = teamItems.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toList())
                        .get(0).getKey();
                bingo.getEndingCoutdown().teamWin(winner);
                Bukkit.broadcast(StringData.getPrefix()
                        .append(Component.text("Die Spielzeit ist abgelaufen, das Team mit den meißten Items hat gewonnen.").color(NamedTextColor.GRAY)));

//                for (Player player : Bukkit.getOnlinePlayers()) {
//                    player.teleport(GameData.getLobbyLocation());
//                    player.getInventory().clear();
//                    player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA, Component.text("Zurück zur Lobby").color(NamedTextColor.GRAY), 1));
//                }
//                GameState.state = GameState.END;
//                Bingo.getBingo().getEndingCoutdown().startPhase();
            }

            counter--;
        }, 20L, 20L);
    }
}
