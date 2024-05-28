package de.nehlen.bingo.inventroy;

import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.util.Items;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.inventory.AbstractMultiPageInventory;
import de.nehlen.spookly.inventory.HandleResult;
import de.nehlen.spookly.team.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.ipvp.canvas.ClickInformation;

import java.util.ArrayList;
import java.util.UUID;

public class TeamSelectInventory extends AbstractMultiPageInventory {

    public TeamSelectInventory(Integer size, Player player) {
        super(size, Component.translatable("gamemode.general.lobby.teamSelect"), player);
        addItems();
    }

    private void addItems() {
        Spookly.getServer().getTeamManager().registeredTeams().forEach(team -> {
            ArrayList<Component> lore = new ArrayList<>();
            lore.add(Component.text(team.size()).decoration(TextDecoration.ITALIC, false).color(StringData.getHighlightColor())
                    .append(Component.text("/").style(Style.style(NamedTextColor.GRAY)))
                    .append(Component.text(team.maxTeamSize()).color(StringData.getHighlightColor())));
            team.registeredPlayers().forEach(teamPlayer -> {
                lore.add(Component.text("- ").style(Style.style(NamedTextColor.GRAY))
                        .append(teamPlayer.displayName().decoration(TextDecoration.ITALIC, false)));
            });
            if(team.registeredPlayers().contains(player)) {
                add(Items.createLore(Material.LIME_DYE, team.teamName(), lore, 1), HandleResult.DENY_GRABBING);
            } else if (team.size().equals(team.maxTeamSize())) {
                add(Items.createLore(Material.RED_DYE, team.teamName(), lore, 1), HandleResult.DENY_GRABBING);
            } else {
                lore.add(Component.empty());
                lore.add(Component.translatable("gamemode.general.invenotry.team.join").color(NamedTextColor.GRAY));
                add(Items.createLore(Material.LIGHT_GRAY_DYE, team.teamName(), lore, 1), HandleResult.DENY_GRABBING, this::handleItemClick, team.uuid().toString());
            }
        });
    }

    private void handleItemClick(Player player, ClickInformation clickInformation) {
        UUID teamUuid = UUID.fromString(clickInformation.getClickedSlot().getSettings().getItemArguments().get(0));
        Team team = Spookly.getServer().getTeamManager().registeredTeams().stream().filter(t -> t.uuid().equals(teamUuid)).toList().get(0);

        GameData.getTeamCache().put(player, team);
        team.registerPlayer(player);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 0);
        player.closeInventory();
    }
}
