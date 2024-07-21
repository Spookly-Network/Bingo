package de.spookly.bingo.inventroy;

import net.kyori.adventure.text.Component;

import de.spookly.canvas.Menu;
import de.spookly.canvas.slot.ClickOptions;
import de.spookly.canvas.slot.Slot;
import de.spookly.canvas.slot.SlotSettings;
import de.spookly.canvas.type.BoxMenu;
import de.spookly.inventory.HandleResult;
import de.spookly.inventory.SinglePageInventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class AbstractDropperInventory implements SinglePageInventory {

	private Menu menu;
	protected Player player;


	public AbstractDropperInventory(Component title, Player player) {
		this.menu = BoxMenu.builder(InventoryType.DROPPER)
				.title(title)
				.build();
		this.player = player;
	}

	@Override
	public void set(ItemStack itemStack, Integer integer, HandleResult handleResult) {
		set(itemStack, integer, handleResult, null);
	}

	@Override
	public void set(ItemStack itemStack, Integer integer, HandleResult handleResult, Slot.ClickHandler clickHandler) {
		this.set(itemStack, integer, handleResult, clickHandler, "");
	}

	public void set(ItemStack itemStack, Integer slotIndex, HandleResult handleResult, Slot.ClickHandler handler, String... arguments) {
		ClickOptions.Builder clickOptions = ClickOptions.builder();
		Slot slot = this.menu.getSlot(slotIndex);
		if (handleResult.equals(HandleResult.ALLOW_GRABBING)) {
			clickOptions = clickOptions.allow(ClickType.LEFT, ClickType.RIGHT).allow(InventoryAction.PLACE_ALL, InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME);
		}

		slot.setSettings(SlotSettings.builder().clickHandler(handler).clickOptions(clickOptions.build()).arguments(arguments).item(itemStack).build());
	}

	public void open() {
		this.menu.open(this.player());
	}

	@Override
	public Player player() {
		return this.player;
	}

	@Override
	public Menu menu() {
		return this.menu;
	}
}
