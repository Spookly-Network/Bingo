package de.nehlen.bingo.inventroy;

import de.nehlen.bingo.data.GameData;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.inventory.AbstractMultiPageInventory;
import de.nehlen.spookly.inventory.HandleResult;
import de.nehlen.spookly.player.SpooklyPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.ipvp.canvas.ClickInformation;

import java.util.UUID;

public class SpectatorInventory extends AbstractMultiPageInventory {

    public SpectatorInventory(Player player) {
        super(2, Component.translatable("gamemode.general.lobby.spectator"), player);
        addItems();
    }

    private void addItems() {
        GameData.getIngame().forEach(onlinePlayer -> {
            SpooklyPlayer spooklyPlayer = Spookly.getPlayer(player);
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwningPlayer(onlinePlayer);
            meta.displayName(spooklyPlayer.nameTag());
            item.setItemMeta(meta);


            add(item, HandleResult.DENY_GRABBING, this::teleportSpectatorToPlayer, player.getUniqueId().toString());
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
