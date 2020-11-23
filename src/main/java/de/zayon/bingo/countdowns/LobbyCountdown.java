package de.zayon.bingo.countdowns;

import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.data.TeamData;
import de.zayon.bingo.data.helper.Team;
import de.zayon.bingo.factory.UserFactory;
import de.zayon.bingo.util.UtilFunctions;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class LobbyCountdown {

    private final Bingo bingo;

    public LobbyCountdown(Bingo bingo) {
        this.bingo = bingo;
    }

    public static int counter = GameData.getStartTime();
    static int scheduler = 0;

    public static void startLobbyCountdown(boolean fast) {
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
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage(StringData.getPrefix() + "§7Das Spiel wird gestartet.");
                        Bukkit.broadcastMessage(StringData.getPrefix() + "§7Spieler Online: " + StringData.getHighlightColor() + Bukkit.getOnlinePlayers().size() + "§7/" + StringData.getHighlightColor() + (GameData.getTeamAmount() * GameData.getTeamSize()) + "§7.");
                        Bukkit.broadcastMessage(StringData.getPrefix() + "§7Map: " + StringData.getHighlightColor() + GameData.getMapName() + " §7gebaut von " + StringData.getHighlightColor() + GameData.getMapBuilder() + "§7.");
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

                            //SET WORLD AND LOCATION SETTINGS
                            Bukkit.getScheduler().runTaskAsynchronously(Bingo.getBingo(), () -> {
                                //SET WORLD SETTINGS
                                World world = Bukkit.getWorld("world");
                                world.getWorldBorder().setCenter(new Location(world, 0, 0, 0));
                                world.getWorldBorder().setSize(GameData.getWorldSize());

                                //TODO Needs work / seems not functional
                                //SET TEAM SPAWNPOINT AND AVOID noSpawnBioms
                                for (Team t : TeamData.getTeamCache()) {
                                    Location block = UtilFunctions.getRandomLocation("world");
                                    while (GameData.getNoSpawnBiomes().contains(block.getBlock().getBiome().toString())) {
                                        block = UtilFunctions.getRandomLocation("world");
                                    }
                                    t.setSpawnLoc(block);
                                }
                            });

                            // SET CURRENT PLAYERLIST TO GAMEDATA
                            // SET PLAYER IN TEAMS
                            ArrayList<Player> playerList = new ArrayList<>();
                            for (final Player players : Bukkit.getOnlinePlayers()) {
                                players.playSound(players.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);
                                playerList.add(players);
                                Bingo.getBingo().getUserFactory().updateGames(players, UserFactory.UpdateType.ADD, 1);
                                Bingo.getBingo().getScoreboardManager().removeUserScoreboard(players);
                                int i = 0;
                                while (!TeamData.playerTeamCache.containsKey(players)) {
                                    if (TeamData.teamCache.get(i).getSize() < GameData.getTeamSize()) {
                                        TeamData.teamCache.get(i).addMate(players);
                                        TeamData.playerTeamCache.put(players, i);
                                    }

                                    i++;
                                }
                            }
                            GameData.setIngame(playerList);
                            //TELEPORT PLAYER TO WORLD
                            for (Player p : GameData.getIngame()) {
                                Team t = TeamData.getTeamCache().get(TeamData.getPlayerTeamCache().get(p));
                                Location loc = t.getSpawnLoc();
                                p.teleport(loc);
                                p.getInventory().clear();
                                p.setGameMode(GameMode.SURVIVAL);
                            }

                            //SET GAME STATUS TO INGAME
                            BukkitCloudNetHelper.changeToIngame();
                            Bukkit.getScheduler().cancelTask(scheduler);
                            Bingo.getBingo().getIngameCountdown().ingameCountdown();
                            GameState.state = GameState.INGAME;
                            Bukkit.broadcastMessage("SUCCESS");
                        }
                    }
                }

                counter--;
            }
        }, 20L, 20L);
    }


    public static void fillItemList() {

        ArrayList<Material> list = new ArrayList<Material>();
        for (int i = 0; i < 9; i++) {
            Material m = Material.getMaterial(UtilFunctions.getRandomStringOutList(GameData.getItemPool()));
            while (list.contains(m)) {
                m = Material.getMaterial(UtilFunctions.getRandomStringOutList(GameData.getItemPool()));
            }
            list.add(m);
            Bukkit.getConsoleSender().sendMessage(m.toString());
        }
        GameData.setItemsToFind(list);
    }
}



