package de.nehlen.bingo.inventroy;

import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.data.helper.TextComponentHelper;
import de.nehlen.bingo.util.ItemBuilder;
import de.spookly.Spookly;
import de.spookly.inventory.AbstractMultiPageInventory;
import de.spookly.inventory.HandleResult;
import de.spookly.team.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import de.spookly.canvas.ClickInformation;
import de.spookly.canvas.mask.BinaryMask;
import de.spookly.canvas.mask.Mask;
import de.spookly.canvas.mask.RecipeMask;
import de.spookly.canvas.slot.SlotSettings;

import java.util.ArrayList;
import java.util.UUID;

public class TeamSelectInventory extends AbstractMultiPageInventory {

    public TeamSelectInventory(Integer size, Player player) {
        super(size, TextComponentHelper.menuBuilder()
                        .setBackground('\uE101')
                        .setTitle(Component.translatable("gamemode.general.gui.teamSelect"))
                        .build(),
                dimension -> {
                    return BinaryMask.builder(dimension)
                            .pattern("000000000")
                            .pattern("111111111")
                            .build();
                }, player);
        addItems();
    }

    private void addItems() {
        newMenuModifier(menu -> {
            Mask mask = RecipeMask.builder(menu)
                    .item('a', new ItemStack(Material.AIR))
                    .item('c', SlotSettings.builder()
                            .item(ItemBuilder.of(Material.PAPER)
                                    .customModelData(20001)
                                    .displayName(Component.translatable("gamemode.general.gui.close")
                                            .color(NamedTextColor.RED).
                                            decoration(TextDecoration.ITALIC, false))
                                    .build())
                            .clickHandler(this::handleClose)
                            .build())
                    .row(1)
                    .pattern("aaaaaaaac")
                    .build();
            mask.apply(menu);
        });


        int i = 1;
        for (Team team : Spookly.getTeamManager().registeredTeams()) {
            ArrayList<Component> lore = new ArrayList<>();
            lore.add(Component.text(team.size()).decoration(TextDecoration.ITALIC, false).color(StringData.getHighlightColor())
                    .append(Component.text("/").style(Style.style(NamedTextColor.GRAY)))
                    .append(Component.text(team.maxTeamSize()).color(StringData.getHighlightColor())));
            team.registeredPlayers().forEach(teamPlayer -> {
                lore.add(Component.text("- ").style(Style.style(NamedTextColor.GRAY))
                        .append(teamPlayer.displayName().decoration(TextDecoration.ITALIC, false)));
            });
            if (team.registeredPlayers().contains(player())) {
                add(ItemBuilder.of(Material.PAPER)
                        .displayName(team.teamName().decoration(TextDecoration.ITALIC, false))
                        .lore(lore)
                        .amount(1)
                        .customModelData(30000 + (i + 1))
                        .build(), HandleResult.DENY_GRABBING);
            } else if (team.size().equals(team.maxTeamSize())) {
                add(ItemBuilder.of(Material.PAPER)
                        .displayName(team.teamName().decoration(TextDecoration.ITALIC, false))
                        .lore(lore)
                        .amount(1)
                        .customModelData(30019)
                        .build(), HandleResult.DENY_GRABBING);
            } else {
                lore.add(Component.empty());
                lore.add(Component.translatable("gamemode.general.invenotry.team.join").color(NamedTextColor.GRAY));
                add(ItemBuilder.of(Material.PAPER)
                        .displayName(team.teamName().decoration(TextDecoration.ITALIC, false))
                        .lore(lore)
                        .amount(1)
                        .customModelData(30000 + i)
                        .build(), HandleResult.DENY_GRABBING, this::handleItemClick, team.uuid().toString());
            }

            i += 2;
        }
    }

    private void handleItemClick(Player player, ClickInformation clickInformation) {
        UUID teamUuid = UUID.fromString(clickInformation.getClickedSlot().getSettings().getItemArguments().get(0));
        Team team = Spookly.getServer().getTeamManager().registeredTeams().stream().filter(t -> t.uuid().equals(teamUuid)).toList().get(0);

        GameData.getTeamCache().put(player, team);
        team.registerPlayer(player);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 0);
        player.closeInventory();
    }

    private void handleClose(Player player, ClickInformation clickInformation) {
        player.closeInventory();
    }

}
