package de.nehlen.bingo.inventroy;

import de.nehlen.bingo.Bingo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BasicInventory implements Listener {

    private final Bingo bingo;

    public BasicInventory(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("Diese Items musst du Sammeln")) {
            e.setCancelled(true);
        }
        return;
    }
}
