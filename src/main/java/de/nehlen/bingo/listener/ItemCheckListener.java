package de.nehlen.bingo.listener;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.spookly.team.Team;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerBucketFishEvent;

import java.util.List;

@AllArgsConstructor
public class ItemCheckListener implements Listener {

    private final Bingo bingo;

    @EventHandler
    public void handlePickUp(EntityPickupItemEvent event) {
        if (!(GameState.state == GameState.INGAME)) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (!GameData.getItemsToFind().contains(event.getItem().getItemStack().getType())) return;

        Material material = event.getItem().getItemStack().getType();
        Player player = (Player) event.getEntity();
        Team team = GameData.getTeamCache().get(player);
        PickList list = (PickList) team.memory().get("picklist");

        if (!list.getItems().contains(material)) return;
        list.completeMaterial(player, team, material);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handleInventoryClick(InventoryClickEvent event) {
        try {
            if (event.isCancelled()) return;
            if (!(GameState.state == GameState.INGAME)) return;
            if (!(event.getWhoClicked() instanceof Player player)) return;
            if (event.getResult() != Event.Result.ALLOW) return;
            if (event.getAction() == InventoryAction.NOTHING) return;
            if (event.getCurrentItem() == null) return;
            if (!GameData.getItemsToFind().contains(event.getCurrentItem().getType())) return;


            Material material = event.getCurrentItem().getType();
            Team team = GameData.getTeamCache().get(player);
            PickList list = (PickList) team.memory().get("picklist");

            if (!list.getItems().contains(material)) return;
            list.completeMaterial(player, team, material);
        } catch (NullPointerException ignored) {
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handleBucketFill(PlayerBucketFillEvent event) {
        if (event.isCancelled()) return;
        if (!(GameState.state == GameState.INGAME)) return;
        if (!GameData.getItemsToFind().contains(event.getItemStack().getType())) return;

        Material material = event.getItemStack().getType();
        Player player = event.getPlayer();
        Team team = GameData.getTeamCache().get(player);
        PickList list = (PickList) team.memory().get("picklist");

        if (!list.getItems().contains(material)) return;
        list.completeMaterial(player, team, material);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void hand(PlayerBucketEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(GameState.state == GameState.INGAME)) return;
        if (!GameData.getItemsToFind().contains(event.getEntityBucket().getType())) return;

        Material material = event.getEntityBucket().getType();
        Player player = event.getPlayer();
        Team team = GameData.getTeamCache().get(player);
        PickList list = (PickList) team.memory().get("picklist");

        if (!list.getItems().contains(material)) return;
        list.completeMaterial(player, team, material);
    }
}
