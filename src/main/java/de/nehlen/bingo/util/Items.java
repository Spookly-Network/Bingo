package de.nehlen.bingo.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.map.MapView;

import java.util.ArrayList;
import java.util.UUID;

public class Items {
    public static ItemStack createItem(Material material, Component displayName, int amount) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta mitem = item.getItemMeta();
        mitem.displayName(displayName.decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(mitem);
        return item;
    }
    public static ItemStack createItem(Material material, int amount) {
        ItemStack item = new ItemStack(material, amount);
        return item;
    }

    public static ItemStack createLore(Material material, Component displayname, ArrayList<Component> lore, int amount) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.lore(lore);
        itemMeta.displayName(displayname.decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack getCustomSkull(String url, Component name, Integer amount) {

        ItemStack head = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.displayName(name);

        if (url.isEmpty()) return head;
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.getProperties().add(new ProfileProperty("textures", url));
        skullMeta.setPlayerProfile(profile);

        head.setItemMeta(skullMeta);
        return head;
    }
}
