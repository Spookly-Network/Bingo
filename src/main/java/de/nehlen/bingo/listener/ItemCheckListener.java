package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.gameapi.TeamAPI.Team;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

@AllArgsConstructor
public class ItemCheckListener implements Listener {

    private final Bingo bingo;

    @EventHandler
    public void handlePickUp(EntityPickupItemEvent event) {
        if(!(GameState.state == GameState.INGAME)) return;
        if(!(event.getEntity() instanceof Player)) return;
        if(!GameData.getItemsToFind().contains(event.getItem().getItemStack().getType())) return;

        Material material = event.getItem().getItemStack().getType();
        Player player = (Player) event.getEntity();
        Team team = GameData.getTeamCache().get(player);
        PickList list = (PickList) team.memory().get("picklist");

        if(!list.getItems().contains(material)) return;
        list.completeMaterial(player, team, material);
    }

    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event) {
        try {
            if(event.isCancelled()) return;
            if(!(GameState.state == GameState.INGAME)) return;
            if(!(event.getWhoClicked() instanceof Player)) return;
            if(event.getView().getTitle().contains("Gegenstände") ) return;
            if(!GameData.getItemsToFind().contains(event.getCurrentItem().getType())) return;


            Material material = event.getCurrentItem().getType();
            Player player = (Player) event.getWhoClicked();
            Team team = GameData.getTeamCache().get(player);
            PickList list = (PickList) team.memory().get("picklist");

            if(!list.getItems().contains(material)) return;
            list.completeMaterial(player, team, material);
        } catch (NullPointerException ignored) {}
        catch (Exception exception) {
            exception.printStackTrace();
        }

    }
}
