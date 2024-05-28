package de.nehlen.bingo.data.helper;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public class TranslatableHelper {
    public static String getTranslationKey(Material material) {
        try {
            if (material.isItem()) {
                return Bukkit.getUnsafe().getItemTranslationKey(material);
            } else {
                return Bukkit.getUnsafe().getBlockTranslationKey(material);
            }
        } catch (NoSuchMethodError e) {
            if (material.isBlock()) {
                return "block." + material.getKey().getNamespace() + "."+material.getKey().getKey();
            } else {
                return "item." + material.getKey().getNamespace() + "."+material.getKey().getKey();
            }
        }
    }
}
