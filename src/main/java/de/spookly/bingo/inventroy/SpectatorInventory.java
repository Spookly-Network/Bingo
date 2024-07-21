package de.spookly.bingo.inventroy;

import java.util.UUID;

import de.spookly.Spookly;
import de.spookly.bingo.data.GameData;
import de.spookly.canvas.ClickInformation;
import de.spookly.inventory.AbstractMultiPageInventory;
import de.spookly.inventory.HandleResult;
import de.spookly.player.SpooklyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.kyori.adventure.text.Component;

public class SpectatorInventory extends AbstractMultiPageInventory {

    public SpectatorInventory(Player player) {
        super(2, Component.translatable("gamemode.general.lobby.spectator"), player);
        addItems();
    }

    private void addItems() {
        GameData.getIngame().forEach(onlinePlayer -> {
            SpooklyPlayer spooklyPlayer = Spookly.getPlayer(player());
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwningPlayer(onlinePlayer);
            meta.displayName(spooklyPlayer.nameTag());
            item.setItemMeta(meta);


            add(item, HandleResult.DENY_GRABBING, this::teleportSpectatorToPlayer, player().getUniqueId().toString());
        });
    }

    private void teleportSpectatorToPlayer(Player player, ClickInformation clickInformation) {
        UUID uuid = UUID.fromString(clickInformation.getClickedSlot().getSettings().getItemArguments().get(0));
        Player onlinePlayer = Bukkit.getPlayer(uuid);
        if (onlinePlayer != null) {
            player.teleport(onlinePlayer);
        }
    }
}
