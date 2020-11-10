package de.zayon.bingo.util;

import de.zayon.zayon_core_api.util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.UUID;

public class Items {
    public static ItemStack createItem(Material material, String displayname, int amount) {

        ItemStack item = new ItemStack(material, amount);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(displayname);
        item.setItemMeta(mitem);

        return item;
    }

    public static ItemStack createShoes(String displayname, Color color) {
        ItemStack i = new ItemStack(Material.LEATHER_BOOTS, 1);
        LeatherArmorMeta im = (LeatherArmorMeta)i.getItemMeta();
        im.setDisplayName(displayname);
        im.setColor(color);
        i.setItemMeta((ItemMeta)im);
        return i;
    }

    public static ItemStack createHelm(String displayname, Color color) {
        ItemStack i = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta im = (LeatherArmorMeta)i.getItemMeta();
        im.setDisplayName(displayname);
        im.setColor(color);
        i.setItemMeta((ItemMeta)im);
        return i;
    }

    public static ItemStack createItemEnch1(Material material, int subid, String displayname, Enchantment enchantment) {
        ItemStack itemE = new ItemStack(material, 1);
        ItemMeta testEnchantMeta = itemE.getItemMeta();
        testEnchantMeta.addEnchant(enchantment, 1, true);
        testEnchantMeta.setDisplayName(displayname);
        itemE.setItemMeta(testEnchantMeta);
        return itemE;
    }

    public static ItemStack createItemEnch2(Material material, int subid, String displayname, Enchantment enchantment) {
        ItemStack itemE2 = new ItemStack(material, 1);
        ItemMeta testEnchantMeta = itemE2.getItemMeta();
        testEnchantMeta.addEnchant(enchantment, 2, true);
        testEnchantMeta.setDisplayName(displayname);
        itemE2.setItemMeta(testEnchantMeta);
        return itemE2;
    }

    public static ItemStack createItemEnch4(Material material, int subid, String displayname, Enchantment enchantment) {
        ItemStack itemE2 = new ItemStack(material, 1);
        ItemMeta testEnchantMeta = itemE2.getItemMeta();
        testEnchantMeta.addEnchant(enchantment, 4, true);
        testEnchantMeta.setDisplayName(displayname);
        itemE2.setItemMeta(testEnchantMeta);
        return itemE2;
    }

    public static ItemStack createItemLore(Material material, int subid, String displayname, String lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta mitem = item.getItemMeta();
        ArrayList<String> Lore = new ArrayList<>();
        mitem.setLore(Lore);
        mitem.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
        mitem.setUnbreakable(true);
        mitem.setDisplayName(displayname);
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack createItemA(Material material, int subid, String displayname, int anzahl) {
        ItemStack item = new ItemStack(material, anzahl);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(displayname);
        mitem.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
        mitem.setUnbreakable(true);
        item.setItemMeta(mitem);
        return item;
    }

    /**
     * @param Display
     * @param Owner
     * @return
     */
    public static ItemStack createSkull(String Display, String Owner) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta metas = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);

        OfflinePlayer op =  Bukkit.getOfflinePlayer(UUIDFetcher.getUUID(Owner));
        metas.setOwningPlayer(op);

        metas.setDisplayName(Display);
        stack.setItemMeta(metas);
        return stack;
    }

    public static ItemStack createSkullByUUID(String Display, String uuid) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta metas = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);

        OfflinePlayer op =  Bukkit.getOfflinePlayer(UUID.fromString(uuid));
        metas.setOwningPlayer(op);

        metas.setDisplayName(Display);
        stack.setItemMeta(metas);
        return stack;
    }
}
