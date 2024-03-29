package de.nehlen.bingo.inventroy;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.util.Items;
import de.nehlen.gameapi.Gameapi;
import de.nehlen.gameapi.TeamAPI.Team;
import de.nehlen.spooklycloudnetutils.manager.GroupManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TeamSelectInventroy implements Listener {

    private final Bingo bingo;
    private Gameapi gameapi;

    public TeamSelectInventroy(Bingo bingo) {
        this.bingo = bingo;
        this.gameapi = Gameapi.getGameapi();
    }

    public Inventory getInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, getInventorySize(), Component.text("Teamauswahl"));
        for (Team t : Gameapi.getGameapi().getTeamAPI().registeredTeams()) {
            ArrayList<Object> argument = new ArrayList<>();
            ArrayList<Component> lore = new ArrayList<>();

            lore.add(Component.text(t.size()).decoration(TextDecoration.ITALIC, false).color(StringData.getHighlightColor())
                    .append(Component.text("/").style(Style.style(NamedTextColor.GRAY)))
                    .append(Component.text(t.maxTeamSize()).color(StringData.getHighlightColor())));
            t.registeredPlayers().forEach(teamPlayer -> {
                lore.add(Component.text("- ").style(Style.style(NamedTextColor.GRAY))
                        .append(teamPlayer.displayName().decoration(TextDecoration.ITALIC, false)));
            });

            if (t.registeredPlayers().contains(player)) {
                inventory.addItem(Items.createLore(Material.LIME_DYE, t.teamName(), lore, 1));
            } else if (t.size().equals(t.maxTeamSize())) {
                inventory.addItem(Items.createLore(Material.RED_DYE, t.teamName(), lore, 1));
            } else {
                argument.add(t);
                inventory.addItem(Items.createLore(Material.LIGHT_GRAY_DYE, t.teamName(), lore, 1));
            }
        }

        return inventory;
    }

    private int getInventorySize() {
        return (int) Math.min(1, Math.ceil((double) GameData.getTeamAmount() / 9)) * 9;
    }

    @EventHandler
    private void handleItemInteract(InventoryClickEvent event) {
        if (!event.getView().title().contains(Component.text("Teamauswahl"))) return;
        if (!event.getCurrentItem().getType().equals(Material.LIGHT_GRAY_DYE)) {
            event.setCancelled(true);
            return;
        }

        String itemname = event.getCurrentItem().getItemMeta().getDisplayName();
        Player player = (Player) event.getWhoClicked();
        Team team = gameapi.getTeamAPI().registeredTeams().stream().filter(t -> t.getTeamName().equals(itemname)).collect(Collectors.toList()).get(0);

        team.addPlayer(player);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
        event.setCancelled(true);
        player.closeInventory();
    }
}
