package de.nehlen.bingo.inventroy;

import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.bingo.data.helper.TextComponentHelper;
import de.nehlen.bingo.data.helper.TranslatableHelper;
import de.nehlen.bingo.util.ItemBuilder;
import de.nehlen.bingo.util.Items;
import de.nehlen.spookly.inventory.HandleResult;
import de.nehlen.spookly.team.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BingoListInventory extends AbstractDropperInventory {

    public BingoListInventory(Player pLayer) {
        super(TextComponentHelper.customUiInventoryTitle('\uE018'), pLayer);
        addItems();
    }

    private void addItems() {
        if (GameState.state != GameState.INGAME || !GameData.getTeamCache().containsKey(player)) {
            int i = 0;
            for (Material material : GameData.getItemsToFind()) {
                set(Items.createItem(material, 1), i, HandleResult.DENY_GRABBING);
                i++;
            }
            return;
        }

        Team team = GameData.getTeamCache().get(player());
        PickList pickList = (PickList) team.memory().get("picklist");

        int i = 0;
        for (Material material : GameData.getItemsToFind()) {
            if (pickList.getItems().contains(material)) {
                set(ItemBuilder.of(material)
                        .displayName(Component.translatable(TranslatableHelper.getTranslationKey(material))
                                .color(NamedTextColor.RED)
                                .decoration(TextDecoration.ITALIC, false))
                        .amount(1)
                        .build(), i, HandleResult.DENY_GRABBING);
                i++;
                continue;
            }
            set(ItemBuilder.of(Material.PAPER)
                    .displayName(Component.translatable("gamemode.bingo.inventory.items.done").color(NamedTextColor.GREEN))
                    .customModelData(10001)
                    .amount(1)
                    .build(), i, HandleResult.DENY_GRABBING);
            i++;
        }
    }
}
