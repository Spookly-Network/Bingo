package de.spookly.bingo.inventroy;

import java.util.List;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import de.spookly.bingo.data.GameData;
import de.spookly.bingo.data.GameState;
import de.spookly.bingo.data.helper.PickList;
import de.spookly.bingo.data.helper.TextComponentHelper;
import de.spookly.bingo.data.helper.TranslatableHelper;
import de.spookly.bingo.util.ItemBuilder;
import de.spookly.bingo.util.Items;
import de.spookly.canvas.ClickInformation;
import de.spookly.inventory.AbstractSinglePageInventory;
import de.spookly.inventory.HandleResult;
import de.spookly.team.Team;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class BingoListInventory extends AbstractSinglePageInventory {

	private static final List<Integer> SLOTS = List.of(12, 13, 14, 21, 22, 23, 30, 31, 32);


	public BingoListInventory(Player player) {
		super(4, TextComponentHelper.menuBuilder()
				.setBackground('\uE018')
				.setTitle(Component.translatable("gamemode.bingo.gui.card"))
				.build(), player);
		addItems();
	}

	private void addItems() {
		addCloseItem();

		if (GameState.state != GameState.INGAME || !GameData.getTeamCache().containsKey(player())) {
			addItemsToFind();
			return;
		}

		addItemsForTeam();
	}

	private void addCloseItem() {
		set(ItemBuilder.of(Material.PAPER)
				.customModelData(20001)
				.displayName(Component.translatable("gamemode.general.gui.close")
						.color(NamedTextColor.RED).
						decoration(TextDecoration.ITALIC, false))
				.build(), 8, HandleResult.DENY_GRABBING, this::handleClose);
	}

	private void addItemsToFind() {
		int i = 0;
		for (Material material : GameData.getItemsToFind()) {
			set(Items.createItem(material, 1), SLOTS.get(i), HandleResult.DENY_GRABBING);
			i++;
		}
	}

	private void addItemsForTeam() {
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
						.build(), SLOTS.get(i), HandleResult.DENY_GRABBING);
				i++;
				continue;
			}
			set(ItemBuilder.of(Material.PAPER)
					.displayName(Component.translatable("gamemode.bingo.inventory.items.done").color(NamedTextColor.GREEN))
					.customModelData(10001)
					.amount(1)
					.build(), SLOTS.get(i), HandleResult.DENY_GRABBING);
			i++;
		}
	}

	private void handleClose(@NotNull Player player, ClickInformation clickInformation) {
		player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.MASTER, 1, 1);
		player.closeInventory();
	}
}
