package de.nehlen.bingo.phases;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.bingo.factory.UserFactory;
import de.nehlen.bingo.util.UtilFunctions;
import de.nehlen.gameapi.Gameapi;
import de.nehlen.gameapi.PhaseApi.AbstractGamePhase;
import de.nehlen.gameapi.TeamAPI.Team;
import de.nehlen.spooklycloudnetutils.CloudGameUtils;
import de.nehlen.spooklycloudnetutils.SpooklyCloudNetUtils;
import de.nehlen.spooklycloudnetutils.helper.CloudStateHelper;
import de.nehlen.spooklycloudnetutils.manager.GroupManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;

public class LobbyCountdown extends AbstractGamePhase {

    private final Bingo bingo;

    public LobbyCountdown(Bingo bingo) {
        super(GameData.getStartTime());
        this.bingo = bingo;
    }

    @Override
    public void startPhase() {
        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bingo.getBingo(), () -> {
            if (counter >= 0) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendActionBar(Component.text("Noch ").color(NamedTextColor.GRAY)
                            .append(Component.text(counter).color(NamedTextColor.WHITE))
                            .append(Component.text(" Sekunden...").color(NamedTextColor.GRAY)));
                }

                if (counter == 60 || counter == 30 || counter == 15 || counter == 10) {
                    Bukkit.broadcast(StringData.getPrefix()
                            .append(Component.text("Das Spiel startet in ").color(NamedTextColor.GRAY))
                            .append(Component.text(counter).color(StringData.getHighlightColor()))
                            .append(Component.text(" Sekunden!").color(NamedTextColor.GRAY)));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        sendCounterTitle(players);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10.0F, 1.0F);
                    }

                } else if (counter == 5) {

                    //SET TEAM SPAWNPOINT AND AVOID noSpawnBioms
                    Bukkit.getScheduler().runTaskAsynchronously(Bingo.getBingo(), () -> {
                        for (Team team : Gameapi.getGameapi().getTeamAPI().registeredTeams()) {
//                            PickList pickList = new PickList(GameData.getItemsToFind());
                            Location block = UtilFunctions.getRandomLocation("world");
                            while (GameData.getNoSpawnBiomes().contains(block.getBlock().getBiome().toString())) {
                                block = UtilFunctions.getRandomLocation("world");
                            }
                            team.addToMemory("spawnLoc", block);
                        }
                    });

                    Bukkit.broadcast(Component.empty());
                    Bukkit.broadcast(StringData.getPrefix().append(Component.text("Das Spiel wird gestartet.").color(NamedTextColor.GRAY)));
                    Bukkit.broadcast(StringData.getPrefix()
                            .append(Component.text("Spieler Online: ").color(NamedTextColor.GRAY))
                            .append(Component.text(Bukkit.getOnlinePlayers().size()).color(StringData.getHighlightColor()))
                            .append(Component.text("/").color(NamedTextColor.GRAY))
                            .append(Component.text((GameData.getTeamAmount() * GameData.getTeamSize())).color(StringData.getHighlightColor()))
                            .append(Component.text(".").color(NamedTextColor.GRAY)));
                    Bukkit.broadcast(StringData.getPrefix()
                            .append(Component.text("Das Spiel startet in ").color(NamedTextColor.GRAY))
                            .append(Component.text(counter).color(StringData.getHighlightColor()))
                            .append(Component.text(" Sekunden!").color(NamedTextColor.GRAY)));
                    Bukkit.broadcast(Component.empty());

                    Bukkit.getOnlinePlayers().forEach(players -> {
                        sendCounterTitle(players);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10.0F, 1.0F);
                    });
                } else if (counter == 0) {

                    //MIN ANZAHL AN SPIELERN
                    if (Bukkit.getOnlinePlayers().size() < (GameData.getTeamSize() + 1)) {
                        counter = 60;
                        Bukkit.broadcast(StringData.getPrefix()
                                .append(Component.text("Es sind nicht genug Spieler Online. Der Countdown startet neu.").color(NamedTextColor.GRAY)));
                    } else {


                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "recipe give @a *");
                        Bukkit.getScheduler().runTaskAsynchronously(Bingo.getBingo(), () -> {
                            // SET CURRENT PLAYERLIST TO GAMEDATA
                            // SET PLAYER IN TEAMS
                            ArrayList<Player> playerList = new ArrayList<>();
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                player.getInventory().clear();

                                playerList.add(player);
                                Bingo.getBingo().getUserFactory().updateGames(player, UserFactory.UpdateType.ADD, 1);
                                Bingo.getBingo().getScoreboardManager().removeUserScoreboard(player); //?
                                if (!GameData.getTeamCache().containsKey(player)) {
                                    Team team = Gameapi.getGameapi().getTeamAPI().addToLowestTeam(player);
                                    GameData.getTeamCache().put(player, team);
                                }
                            });
                            GameData.setIngame(playerList);
                            Gameapi.getGameapi().getTeamAPI().removeEmptyTeams();

                            //TELEPORT PLAYER TO WORLD
                            Gameapi.getGameapi().getTeamAPI().registeredTeams().forEach(team -> {
//                                Location loc = (Location) team.getMemory().get("spawnLoc");
                                team.registeredPlayers().forEach(player -> {
//                                    UtilFunctions.ActionBar(player, "§7Du wirst teleportiert...");
                                    Bukkit.getScheduler().runTask(this.bingo, () -> {
                                        player.getInventory().clear();
//                                        player.teleport(loc);
                                        player.setGameMode(GameMode.SURVIVAL);
                                    });
                                });
                            });

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

    public static void fillItemList() {
        ArrayList<Material> list = new ArrayList<Material>();
        for (int i = 0; i < GameData.getItemsAmount(); i++) {
            Material m = null;
            while (m == null || list.contains(m)) {
                m = Material.getMaterial(UtilFunctions.getRandomStringOutList(GameData.getItemPool()));
            }
            list.add(m);
        }
        GameData.setItemsToFind(list);
    }

    private void sendCounterTitle(Player player) {
        player.showTitle(Title.title(Component.text(counter()).append(Component.text(" Sek.").color(NamedTextColor.GRAY)),
                Component.text("bis zum Start...").color(NamedTextColor.GRAY),
                Title.Times.times(Duration.ofSeconds(Double.valueOf(.5).longValue()), Duration.ofSeconds(1), Duration.ofSeconds(1))));
    }
}