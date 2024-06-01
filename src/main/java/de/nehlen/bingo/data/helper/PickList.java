package de.nehlen.bingo.data.helper;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.factory.UserFactory;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.player.SpooklyPlayer;
import de.nehlen.spookly.team.Team;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PickList {

    @Getter
    private List<Material> items;

    public PickList(List<Material> items) {
        this.items = new ArrayList<>(items);
    }

    public void completeMaterial(Player player, Team team, Material material) {
        SpooklyPlayer spooklyPlayer = Spookly.getPlayer(player);

        if (!GameData.firstItemFound) {
            spooklyPlayer.addPoints(20);
            player.sendMessage(TextComponentHelper.addPointsComponent(20));
            player.sendMessage(StringData.getPrefix()
                    .append(Component.text("Du hast den ").color(NamedTextColor.GRAY)
                            .append(Component.text("ersten Gegenstand").color(StringData.getHighlightColor()))
                            .append(Component.text(" der Runde gefunden.").color(NamedTextColor.GRAY))));
            GameData.firstItemFound=true;
        }

        spooklyPlayer.addPoints(20);
        player.sendMessage(TextComponentHelper.addPointsComponent(20));
        Bingo.getBingo().getUserFactory().updateCraftedItems(player, UserFactory.UpdateType.ADD, 1);
        items.remove(material);

        Bukkit.broadcast(StringData.getPrefix()
                .append(Component.translatable("phase.ingame.itemFound",
                        spooklyPlayer.nameTag(),
                        Component.translatable(TranslatableHelper.getTranslationKey(material)).color(StringData.getHighlightColor()),
                        Component.text(getAmountCompleted()),
                        Component.text(GameData.getItemsAmount())
                        ).color(NamedTextColor.GRAY)));
        if (isComplete()) {
            Bingo.getBingo().getIngameCountdown().endPhase();
            Bingo.getBingo().getEndingPhase().teamWin(team);
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
        return "PickList[" + stringBuilder.toString() + "]";
    }

}
