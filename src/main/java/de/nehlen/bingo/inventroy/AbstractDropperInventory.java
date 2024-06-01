package de.nehlen.bingo.inventroy;

import de.nehlen.spookly.inventory.AbstractSinglePageInventory;
import de.nehlen.spookly.inventory.HandleResult;
import de.nehlen.spookly.inventory.SinglePageInventory;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.slot.ClickOptions;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.slot.SlotSettings;
import org.ipvp.canvas.type.BoxMenu;

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
