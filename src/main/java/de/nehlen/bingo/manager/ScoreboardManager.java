package de.nehlen.bingo.manager;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.util.fonts.TeamFont;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.IngameScoreboardData;
import de.nehlen.bingo.data.ScoreboardData;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.bingo.data.helper.TranslatableHelper;
import de.nehlen.bingo.sidebar.Sidebar;
import de.nehlen.bingo.sidebar.SidebarCache;
import de.nehlen.bingo.util.UtilFunctions;
import de.nehlen.spookly.team.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class ScoreboardManager {
    private final Bingo bingo;

    private final HashMap<Player, BukkitTask> scoreboardTaskMap = new HashMap<>();

    private final int updateInterval = 1;

    public ScoreboardManager(Bingo bingo) {
        this.bingo = bingo;
    }

    public void setUserScoreboard(final Player player) {
        if (!this.scoreboardTaskMap.containsKey(player))
            this.scoreboardTaskMap.put(player, (new BukkitRunnable() {
                int counter = 0;
                int sec = 0;

                public void run() {
                    if (GameState.state == GameState.INGAME) {
                        ScoreboardManager.this.setIngameScoreboardContent(player);
                    } else {
                        ScoreboardManager.this.setScoreboardContent(player, this.counter);
                    }

                    if (sec >= 15) {
                        this.counter = ++this.counter % (ScoreboardData.values()).length;
                        sec = 0;
                    }
                    sec++;
                }
            }).runTaskTimer((Plugin) this.bingo, 0L, 20L));
    }

    public void removeUserScoreboard(Player player) {
        if (this.scoreboardTaskMap.containsKey(player)) {
            ((BukkitTask) this.scoreboardTaskMap.get(player)).cancel();
            this.scoreboardTaskMap.remove(player);
        }
    }

    private void setScoreboardContent(Player player, int pageNumber) {
        ScoreboardData scoreboardData = ScoreboardData.values()[pageNumber];
//        this.bingo.getSidebarCache();
        Sidebar sidebar = SidebarCache.getUniqueCachedSidebar(player);
        sidebar.setDisplayName(scoreboardData.getDisplayName());
        scoreboardData.getLines().forEach(line -> {
            line.replaceText(replaceMaterial("%item1%", player, 0));
        });
        sidebar.setLines(scoreboardData.getLines().stream().map(line -> {
            return line.replaceText(replaceMaterial("%item1%", player, 0))
                    .replaceText(replaceMaterial("%item2%", player, 1))
                    .replaceText(replaceMaterial("%item3%", player, 2))
                    .replaceText(replaceMaterial("%item4%", player, 3))
                    .replaceText(replaceMaterial("%item5%", player, 4))
                    .replaceText(replaceMaterial("%item6%", player, 5))
                    .replaceText(replaceMaterial("%item7%", player, 6))
                    .replaceText(replaceMaterial("%item8%", player, 7))
                    .replaceText(replaceMaterial("%item9%", player, 8))
                    .replaceText(replace("%gamestatus%", Component.text(GameState.state.toString())))
                    .replaceText(replace("%timer%", Component.text(UtilFunctions.formatTime(Bingo.getBingo().getIngameCountdown().getCounter()))))
                    .replaceText(replace("%team%", getTeam(player)));
        }).collect(Collectors.toList()));

//        sidebar.setLines(scoreboardData.getLines(),
//                "%item1%", getText(player, 0),
//                "%item2%", getText(player, 1),
//                "%item3%", getText(player, 2),
//                "%item4%", getText(player, 3),
//                "%item5%", getText(player, 4),
//                "%item6%", getText(player, 5),
//                "%item7%", getText(player, 6),
//                "%item8%", getText(player, 7),
//                "%item9%", getText(player, 8),
//                "%gamestatus%", GameState.state.toString(),
//                "%timer%", UtilFunctions.formatTime(Bingo.getBingo().getIngameCountdown().getCounter()),
//                "%team%", getTeam(player)
//        );
    }

    private void setIngameScoreboardContent(Player player) {
        IngameScoreboardData scoreboardData = IngameScoreboardData.PAGE_1;
//        this.bingo.getSidebarCache();
        Sidebar sidebar = SidebarCache.getUniqueCachedSidebar(player);
        sidebar.setDisplayName(scoreboardData.getDisplayName());
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        sidebar.setLines(scoreboardData.getLines().stream().map(line -> {
            return line.replaceText(replaceMaterial("%item1%", player, 0))
                    .replaceText(replaceMaterial("%item2%", player, 1))
                    .replaceText(replaceMaterial("%item3%", player, 2))
                    .replaceText(replaceMaterial("%item4%", player, 3))
                    .replaceText(replaceMaterial("%item5%", player, 4))
                    .replaceText(replaceMaterial("%item6%", player, 5))
                    .replaceText(replaceMaterial("%item7%", player, 6))
                    .replaceText(replaceMaterial("%item8%", player, 7))
                    .replaceText(replaceMaterial("%item9%", player, 8));
        }).collect(Collectors.toList()));

//        sidebar.setLines(scoreboardData.getLines(),
//                "%item1%", getText(player, 0),
//                "%item2%", getText(player, 1),
//                "%item3%", getText(player, 2),
//                "%item4%", getText(player, 3),
//                "%item5%", getText(player, 4),
//                "%item6%", getText(player, 5),
//                "%item7%", getText(player, 6),
//                "%item8%", getText(player, 7),
//                "%item9%", getText(player, 8)
//        );
    }

    public Component getTeam(Player player) {
        if (GameData.getTeamCache().containsKey(player)) {
            return GameData.getTeamCache().get(player).prefix().font(TeamFont.KEY);
        } else {
            return Component.text("Kein Team");
        }
    }

    private Component getTextComponent(Player player, Integer index) {
        try {
            Material material = GameData.getItemsToFind().get(index);
            Component prefix = Component.text("- ").color(NamedTextColor.GRAY);
            Component itemName = Component.translatable(Objects.requireNonNull(TranslatableHelper.getTranslationKey(material)));
            TextColor color = NamedTextColor.RED;

            if (GameData.getTeamCache().containsKey(player) && GameState.state != GameState.LOBBY) {
                Team team = GameData.getTeamCache().get(player);
                PickList pickList = (PickList) team.memory().get("picklist");
                if (!pickList.getItems().contains(material)) {
                    color = NamedTextColor.GREEN;
                    prefix = Component.text("✔ ").color(NamedTextColor.GREEN);
                }
            } else {
                color = NamedTextColor.GRAY;
            }

            return prefix.append(itemName.color(color));
        } catch (IndexOutOfBoundsException ignored) {
            return Component.empty();
        }
    }

    private String getText(Player player, Integer index) {
        try {
            Material material = GameData.getItemsToFind().get(index);
            String itemName = material.name().replace('_', ' ').toLowerCase(Locale.GERMANY);
            String color = "§c";
            String prefix = "§7- ";

            if (GameData.getTeamCache().containsKey(player) && GameState.state != GameState.LOBBY) {
                Team team = GameData.getTeamCache().get(player);
                PickList pickList = (PickList) team.memory().get("picklist");
                if (!pickList.getItems().contains(material)) {
                    color = "§a";
                    prefix = "§a✔ ";
                }
            } else {
                color = "§7";
            }
            String combiened = prefix + color + StringUtils.capitalize(itemName);

            if(combiened.length() > 27) {
                combiened = combiened.substring(0, 27) + "...";
            }

            return combiened;
        } catch (IndexOutOfBoundsException ignored) {
            return "";
        }
    }

    private TextReplacementConfig replaceMaterial(String literal, Player player, Integer listIndex) {
        return TextReplacementConfig.builder()
                .matchLiteral(literal)
                .replacement(getTextComponent(player, listIndex))
                .build();
    }

    private TextReplacementConfig replace(String literal, Component component) {
        return TextReplacementConfig.builder()
                .matchLiteral(literal)
                .replacement(component)
                .build();
    }
}
