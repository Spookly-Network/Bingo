package de.nehlen.bingo.phases;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.factory.UserFactory;
import de.nehlen.bingo.util.AbstractGamePhase;
import de.nehlen.bingo.util.UtilFunctions;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.team.Team;
import de.nehlen.spooklycloudnetutils.helper.CloudStateHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class LobbyPhase extends AbstractGamePhase {

    private final Bingo bingo;

    public LobbyPhase(Bingo bingo) {
        super(GameData.getStartTime());
        this.bingo = bingo;
    }

    @Override
    public void startPhase() {
        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bingo.getBingo(), () -> {
            if (counter >= 0) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendActionBar(secondsLeftComponent());
                }

                if (counter == 60 || counter == 30 || counter == 15 || counter == 10) {
                    Bukkit.broadcast(startsInComponent());
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.showTitle(sendCounterTitle());
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10.0F, 1.0F);
                    }

                } else if (counter == 5) {

                    //SET TEAM SPAWNPOINT AND AVOID noSpawnBioms
                    Bukkit.getScheduler().runTaskAsynchronously(bingo, () -> {
                        Spookly.getTeamManager().registeredTeams().forEach(team -> {
                            Location block;
                            do {
                                block = UtilFunctions.getRandomLocation("world");
                            } while (GameData.getNoSpawnBiomes().contains(block.getBlock().getBiome().toString()));
                            team.addToMemory("spawnLoc", block);
                        });
                    });

                    Bukkit.broadcast(Component.empty());
                    Bukkit.broadcast(StringData.getPrefix().append(Component.translatable("bingo.phase.lobby.starts").color(NamedTextColor.GRAY)));
                    Bukkit.broadcast(StringData.getPrefix()
                            .append(Component.translatable("bingo.phase.lobby.playersOnline").color(NamedTextColor.GRAY))
                            .append(Component.text(Bukkit.getOnlinePlayers().size()).color(StringData.getHighlightColor()))
                            .append(Component.text("/").color(NamedTextColor.GRAY))
                            .append(Component.text((GameData.getTeamAmount() * GameData.getTeamSize())).color(StringData.getHighlightColor()))
                            .append(Component.text(".").color(NamedTextColor.GRAY)));
                    Bukkit.broadcast(startsInComponent());
                    Bukkit.broadcast(Component.empty());

                    Bukkit.getOnlinePlayers().forEach(players -> {
                        players.showTitle(sendCounterTitle());
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10.0F, 1.0F);
                    });
                } else if (counter == 0) {

                    //MIN ANZAHL AN SPIELERN
                    if (Bukkit.getOnlinePlayers().size() < GameData.getMinPlayerToStartGame()) {
                        counter = 60;
                        Bukkit.broadcast(StringData.getPrefix()
                                .append(Component.translatable("bingo.phase.lobby.notEnoughPlayers").color(NamedTextColor.GRAY)));
                    } else {


                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "recipe give @a *");
                        Bukkit.getScheduler().runTaskAsynchronously(Bingo.getBingo(), () -> {
                            // SET CURRENT PLAYERLIST TO GAMEDATA
                            // SET PLAYER IN TEAMS
                            ArrayList<Player> playerList = new ArrayList<>();
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                Bukkit.getScheduler().runTask(bingo, () -> {
                                    player.getInventory().clear();
                                    player.setGameMode(GameMode.SURVIVAL);
                                });

                                playerList.add(player);
                                Bingo.getBingo().getUserFactory().updateGames(player, UserFactory.UpdateType.ADD, 1);
                                Bingo.getBingo().getScoreboardManager().removeUserScoreboard(player); //?
                                if (!GameData.getTeamCache().containsKey(player)) {
                                    Team team = Spookly.getTeamManager().registerPlayerToLowestTeam(player);
                                    GameData.getTeamCache().put(player, team);
                                }
                            });
                            GameData.setIngame(playerList);
                            Spookly.getTeamManager().removeEmptyTeams();


                            //SET GAME STATUS TO INGAME
                            CloudStateHelper.changeServiceToIngame();
                            this.endPhase();
                            bingo.getTeleportPhase().startPhase();
//                            this.bingo.getIngameCountdown().startPhase();
                        });
                    }
                }
            }
            counter--;
        }, 20L, 20L);
    }

    public static List<Material> fillItemList() {
        ArrayList<Material> list = new ArrayList<Material>();
        for (int i = 0; i < GameData.getItemsAmount(); i++) {
            Material m = null;
            while (m == null || list.contains(m)) {
                m = Material.getMaterial(UtilFunctions.getRandomStringOutList(GameData.getItemPool()));
            }
            list.add(m);
        }
        GameData.setItemsToFind(list);
        return list;
    }

    private Component secondsLeftComponent() {
        return Component.translatable("bingo.lobby.secondsLeft", Component.text(counter()).color(StringData.getHighlightColor()))
                .color(NamedTextColor.GRAY);
    }

    private Component startsInComponent() {
        return StringData.getPrefix()
                .append(Component.translatable("bingo.lobby.startsIn",
                                Component.text(counter()).color(StringData.getHighlightColor()))
                        .color(NamedTextColor.GRAY));
    }

    private Title sendCounterTitle() {
        return Title.title(Component.translatable("bingo.phase.lobby.CounterTitle", Component.text(counter()).color(StringData.getHighlightColor())).color(NamedTextColor.GRAY),
                Component.translatable("bingo.phase.lobby.CounterSubTitle").color(NamedTextColor.GRAY),
                Title.Times.times(Duration.ofSeconds(Double.valueOf(.5).longValue()), Duration.ofSeconds(1), Duration.ofSeconds(1)));
    }
}