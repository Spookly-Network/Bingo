package de.nehlen.bingo.inventroy;

import de.exceptionflug.mccommons.config.shared.ConfigFactory;
import de.exceptionflug.mccommons.config.shared.ConfigWrapper;
import de.exceptionflug.mccommons.config.spigot.SpigotConfig;
import de.exceptionflug.mccommons.inventories.api.Arguments;
import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.api.InventoryType;
import de.exceptionflug.mccommons.inventories.spigot.design.SpigotOnePageInventoryWrapper;
import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.StringData;
import de.nehlen.gameapi.Gameapi;
import de.nehlen.gameapi.ItemsAPI.Items;
import de.nehlen.gameapi.TeamAPI.Team;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TeamSelectInventroy extends SpigotOnePageInventoryWrapper {

    private static final ConfigWrapper CONFIG_WRAPPER = ConfigFactory.create(Bingo.getBingo().getDescription().getName() + "/inventories", TeamSelectInventroy.class, SpigotConfig.class);

    public TeamSelectInventroy(Player player) {
        super(player, CONFIG_WRAPPER);
        setInventoryType(InventoryType.getChestInventoryWithRows((int)Math.min(1, Math.ceil(GameData.getTeamAmount() / 9))));
        setTitle("§7Teamauswahl");

        for (Team t : Gameapi.getGameapi().getTeamAPI().getRegisteredTeams()) {
            ArrayList<Object> argument = new ArrayList<>();
            ArrayList<String> lore = new ArrayList<>();

            lore.add(StringData.getHighlightColor() + t.size() + "§7/" + StringData.getHighlightColor() + t.getMaxTeamSize());
            t.getRegisteredPlayers().forEach(teamPlayer -> {
                lore.add("§7- " + teamPlayer.getDisplayName());
            });

            if (t.getRegisteredPlayers().contains(player)) {
                add(Items.createLore(Material.LIME_DYE, t.getTeamName(), lore, 1), "nothing");
            } else if (t.size().equals(t.getMaxTeamSize())) {
                add(Items.createLore(Material.RED_DYE, t.getTeamName(), lore, 1), "nothing");
            } else {
                argument.add(t);
                add(Items.createLore(Material.LIGHT_GRAY_DYE, t.getTeamName(), lore, 1), "selectTeam", new Arguments(argument));
            }
        }
    }

    public void updateInventory() {
        super.updateInventory();
    }

    public void registerActionHandlers() {
        registerActionHandler("nothing", click -> {
            return CallResult.DENY_GRABBING;
        });
        registerActionHandler("selectTeam", click -> {
            Player player = getPlayer();
            Team argument = click.getArguments().get(0);

            argument.addPlayer(player);
            GameData.getTeamCache().put(player, argument);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
            player.closeInventory();
            return CallResult.DENY_GRABBING;
        });
    }
}
