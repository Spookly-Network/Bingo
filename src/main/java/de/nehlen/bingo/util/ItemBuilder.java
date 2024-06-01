package de.nehlen.bingo.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemBuilder {

    private ItemStack item;
    private ItemBuilder(ItemStack item) {
        this.item = item;
    }

    private ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    @Deprecated
    private ItemBuilder() {}

    public ItemBuilder displayName(Component name) {
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder customModelData(Integer data) {
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(data);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(Component... lore) {
        ItemMeta meta = item.getItemMeta();
        meta.lore(Arrays.stream(lore).collect(Collectors.toList()));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLoreLine(Component line) {
        ItemMeta meta = item.getItemMeta();
        var lore = meta.lore();

        assert lore != null;
        lore.add(line);
        meta.lore(lore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addPotionEffect(PotionEffectType effect, int duration, int amplifier) {
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        assert meta != null;
        meta.addCustomEffect(new PotionEffect(effect, duration, amplifier), true);
        meta.setColor(effect.getColor());
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder potionEffects(List<PotionEffect> effects) {
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        assert meta != null;
        for (PotionEffect effect : effects) {
            meta.addCustomEffect(effect, true);
        }
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder basePotionType(PotionType type) {
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        assert meta != null;
        meta.setBasePotionType(type);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addEnchant(enchantment, level, true);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder potionColor(Color color) {
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        assert meta != null;
        meta.setColor(color);
        item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder skullTexture(String texture) {
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();

        if (texture.isEmpty()) return this;
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.getProperties().add(new ProfileProperty("textures", texture));
        skullMeta.setPlayerProfile(profile);

        item.setItemMeta(skullMeta);
        return this;
    }

    public ItemStack build() {
        return item;
    }

    @Deprecated
    public static ItemBuilder builder() {
        return new ItemBuilder();
    }

    public static ItemBuilder of(ItemStack item) {
        return new ItemBuilder(item);
    }

    public static ItemBuilder of(Material item) {
        return new ItemBuilder(item);
    }
}