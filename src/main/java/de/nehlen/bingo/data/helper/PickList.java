package de.nehlen.bingo.data.helper;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.factory.UserFactory;
import de.nehlen.gameapi.Gameapi;
import de.nehlen.gameapi.PointsAPI.PointsAPI;
import de.nehlen.gameapi.TeamAPI.Team;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PickList {

    @Getter
    private List<Material> items;

    public PickList(List<Material> items) {
        this.items = new ArrayList<>(items);
    }

    public void completeMaterial(Player player, Team team, Material material) {
        if (!GameData.firstItemFound) {
            Gameapi.getGameapi().getPointsAPI().updatePoints(player, PointsAPI.UpdateType.ADD, 100);
            player.sendMessage(TextComponentHelper.addPointsComponent(100));
            player.sendMessage(StringData.getPrefix()
                    .append(Component.text("Du hast den ").color(NamedTextColor.GRAY)
                            .append(Component.text("ersten Gegenstand").color(StringData.getHighlightColor()))
                            .append(Component.text(" der Runde gefunden.").color(NamedTextColor.GRAY))));
            GameData.firstItemFound=true;
        }

        Gameapi.getGameapi().getPointsAPI().updatePoints(player, PointsAPI.UpdateType.ADD, 100);
        player.sendMessage(StringData.getPrefix()
                .append(Component.text("Du hast ").color(NamedTextColor.GRAY))
                .append(Component.text("100").color(NamedTextColor.RED))
                .append(Component.text(" Punkte erhalten.").color(NamedTextColor.GRAY)));
        Bingo.getBingo().getUserFactory().updateCraftedItems(player, UserFactory.UpdateType.ADD, 1);
        items.remove(material);

        Bukkit.broadcast(StringData.getPrefix()
                .append(StringData.playerTeamPrefix(player))
                .append(player.displayName())
                .append(Component.text(" hat ").color(NamedTextColor.GRAY))
                .append(Component.translatable(TranslatableHelper.getTranslationKey(material)).color(StringData.getHighlightColor()))
                .append(Component.text(" gefunden.").color(NamedTextColor.GRAY))
                .append(Component.text(" (" + getAmountCompleted() + "/" + GameData.getItemsAmount() + " Items)").color(NamedTextColor.GRAY)));
        if (isComplete()) {
            Bingo.getBingo().getIngameCountdown().endPhase();
            Bingo.getBingo().getEndingCoutdown().teamWin(team);
        }
    }

    public Integer materialsLeft() {
        return items.size();
    }

    public Integer getAmountCompleted() {
        return GameData.getItemsToFind().size() - items.size();
    }

    public Boolean isComplete() {
        return items.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        items.forEach(material -> {
            stringBuilder.append(material.toString() + ",");
        });
        return stringBuilder.toString();
    }

}
