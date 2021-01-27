package de.zayon.bingo.countdowns;

import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.factory.UserFactory;
import de.zayon.bingo.util.UtilFunctions;
import de.zayon.zayonapi.TeamAPI.Team;
import de.zayon.zayonapi.ZayonAPI;
import io.sentry.Sentry;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class LobbyCountdown {

    private final Bingo bingo;

    public LobbyCountdown(Bingo bingo) {
        this.bingo = bingo;
    }

    public static int counter = GameData.getStartTime();
    public static int scheduler = 0;

    public static void startLobbyCountdown(boolean fast) {
        try {
            if (fast) {
                counter = 10;
            }

            scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bingo.getBingo(), new Runnable() {
                @Override
                public void run() {


                    if (counter >= 0) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            UtilFunctions.ActionBar(p, "§7Noch §f" + counter + " §7Sekunden...");
                        }

                        if (counter == 15 || counter == 10) {
                            Bukkit.broadcastMessage(StringData.getPrefix() + "§7Das Spiel startet in " + StringData.getHighlightColor() + counter + " §7Sekunden!");
                            for (Player players : Bukkit.getOnlinePlayers()) {

                                players.sendTitle(counter + " §7Sek.", "§7bis zum Start...", 5, 20, 5);
                                players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10.0F, 1.0F);
                            }

                        } else if (counter == 5) {

                            //SET TEAM SPAWNPOINT AND AVOID noSpawnBioms
                            Bukkit.getScheduler().runTaskAsynchronously(Bingo.getBingo(), () -> {
                                for (Team team : ZayonAPI.getZayonAPI().getTeamAPI().getRegisteredTeams()) {
                                    Location block = UtilFunctions.getRandomLocation("world");
                                    while (GameData.getNoSpawnBiomes().contains(block.getBlock().getBiome().toString())) {
                                        block = UtilFunctions.getRandomLocation("world");
                                    }
                                    team.addToMemory("spawnLoc", block);
                                }
                            });

                            Bukkit.broadcastMessage("");
                            Bukkit.broadcastMessage(StringData.getPrefix() + "§7Das Spiel wird gestartet.");
                            Bukkit.broadcastMessage(StringData.getPrefix() + "§7Spieler Online: " + StringData.getHighlightColor() + Bukkit.getOnlinePlayers().size() + "§7/" + StringData.getHighlightColor() + (GameData.getTeamAmount() * GameData.getTeamSize()) + "§7.");
                            Bukkit.broadcastMessage(StringData.getPrefix() + "§7Das Spiel startet in " + StringData.getHighlightColor() + counter + " §7Sekunden!");
                            Bukkit.broadcastMessage("");


                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.sendTitle("§f" + counter + " §7Sek.", "§7bis zum Start...", 5, 20, 5);
                                players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10.0F, 1.0F);
                            }
                        } else if (counter == 0) {

                            if (Bukkit.getOnlinePlayers().size() < 2) {
                                counter = 60;
                                Bukkit.broadcastMessage(StringData.getPrefix() + "Es sind nicht genug Spieler Online. Der Countdown startet neu.");
                            } else {

                                Bukkit.broadcastMessage(StringData.getPrefix() + "§7Das Spiel startet " + StringData.getHighlightColor() + "jetzt§!");

                                //Run Tasks Async
                                Bukkit.getScheduler().runTaskAsynchronously(Bingo.getBingo(), () -> {

                                    //SET WORLD SETTINGS
                                    World world = Bukkit.getWorld("world");
                                    world.getWorldBorder().setCenter(new Location(world, 0, 0, 0));
                                    world.getWorldBorder().setSize(GameData.getWorldSize());
                                    world.setDifficulty(Difficulty.EASY);
                                    world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);

                                    // SET CURRENT PLAYERLIST TO GAMEDATA
                                    // SET PLAYER IN TEAMS
                                    ArrayList<Player> playerList = new ArrayList<>();
                                    Bukkit.getOnlinePlayers().forEach(players -> {
                                        players.playSound(players.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);
                                        playerList.add(players);
                                        Bingo.getBingo().getUserFactory().updateGames(players, UserFactory.UpdateType.ADD, 1);
                                        Bingo.getBingo().getScoreboardManager().removeUserScoreboard(players);
                                        if (!GameData.getTeamCache().containsKey(players)) {
                                            Team team = ZayonAPI.getZayonAPI().getTeamAPI().addToLowestTeam(players);
                                            GameData.getTeamCache().put(players, team);
                                        }
                                    });
                                    GameData.setIngame(playerList);

                                    //TELEPORT PLAYER TO WORLD
                                    ZayonAPI.getZayonAPI().getTeamAPI().getRegisteredTeams().forEach(team -> {
                                        Location loc = (Location) team.getMemory().get("spawnLoc");
                                        team.getRegisteredPlayers().forEach(player -> {
                                            UtilFunctions.ActionBar(player, "§7Du wirst teleportiert...");
                                            player.getInventory().clear();
                                            player.setGameMode(GameMode.SURVIVAL);
                                            Bukkit.getScheduler().runTask(ZayonAPI.getZayonAPI(), () -> player.teleport(loc));
                                        });
                                    });


//                                    GameData.getIngame().forEach(player -> {
//                                        Bukkit.getConsoleSender().sendMessage("In teleport");
//                                        Team team = GameData.getTeamCache().get(player);
//                                        Bukkit.getScheduler().runTask(Bingo.getBingo(), () -> {
//                                            Bukkit.getConsoleSender().sendMessage("In SYNC");
//                                            Bukkit.getConsoleSender().sendMessage("--------");
//                                            Bukkit.getConsoleSender().sendMessage(team.getMemory().toString());
//                                            Bukkit.getConsoleSender().sendMessage("0");
//                                            Location loc = (Location) team.getMemory().get("spawnLoc");
//                                            Bukkit.getConsoleSender().sendMessage("1");
//                                            UtilFunctions.ActionBar(player, "§7Du wirst teleportiert...");
//                                            Bukkit.getConsoleSender().sendMessage("2");
//                                            player.teleport(loc);
//                                            Bukkit.getConsoleSender().sendMessage("3");
//                                            Bukkit.getConsoleSender().sendMessage("SYNC End");
//                                        });
//                                        Bukkit.getConsoleSender().sendMessage("OUT SYNC");
//
//                                    });


                                    //SET GAME STATUS TO INGAME
                                    BukkitCloudNetHelper.changeToIngame();
                                    Bukkit.getScheduler().cancelTask(scheduler);
                                    IngameCountdown.ingameCountdown();
                                    GameState.state = GameState.INGAME;

                                });
                            }
                        }
                    }
                    counter--;
                }
            }, 20L, 20L);
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }


    public static void fillItemList() {

        ArrayList<Material> list = new ArrayList<Material>();
        for (int i = 0; i < 9; i++) {
            Material m = Material.getMaterial(UtilFunctions.getRandomStringOutList(GameData.getItemPool()));
            while (list.contains(m)) {
                m = Material.getMaterial(UtilFunctions.getRandomStringOutList(GameData.getItemPool()));
            }
            list.add(m);
        }
        GameData.setItemsToFind(list);
    }
}



