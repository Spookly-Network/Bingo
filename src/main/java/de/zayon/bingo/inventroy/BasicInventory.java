package de.zayon.bingo.inventroy;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import de.zayon.zayonapi.TeamAPI.Team;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
    }
}
