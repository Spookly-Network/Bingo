package de.nehlen.bingo.inventroy;

import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.bingo.data.helper.TranslatableHelper;
import de.nehlen.bingo.util.Items;
import de.nehlen.spookly.inventory.AbstractMultiPageInventory;
import de.nehlen.spookly.inventory.HandleResult;
import de.nehlen.spookly.team.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BingoListInventory extends AbstractMultiPageInventory {

    public BingoListInventory(Player pLayer) {
        super(1, Component.translatable("gamemode.bingo.lobby.items"), pLayer);
        addItems();
    }

    private void addItems() {
        if (GameState.state != GameState.INGAME || !GameData.getTeamCache().containsKey(player)) {
            GameData.getItemsToFind().forEach(material -> {
                add(Items.createItem(material, 1), HandleResult.DENY_GRABBING);
            });
            return;
        }

        Team team = GameData.getTeamCache().get(player());
        PickList pickList = (PickList) team.memory().get("picklist");
        GameData.getItemsToFind().forEach(material -> {
            if(pickList.getItems().contains(material)) {
                add(Items.createItem(material, Component.translatable(TranslatableHelper.getTranslationKey(material)).style(Style.style(NamedTextColor.RED)), 1), HandleResult.DENY_GRABBING);
                return;
            }
            add(Items.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc5YTVjOTVlZTE3YWJmZWY0NWM4ZGMyMjQxODk5NjQ5NDRkNTYwZjE5YTQ0ZjE5ZjhhNDZhZWYzZmVlNDc1NiJ9fX0=",
                    Component.translatable("gamemode.bingo.inventory.items.done").color(NamedTextColor.GREEN), 1), HandleResult.DENY_GRABBING);
        });
    }
}
