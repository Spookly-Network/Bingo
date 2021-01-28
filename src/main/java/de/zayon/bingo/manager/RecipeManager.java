package de.zayon.bingo.manager;

import de.zayon.bingo.Bingo;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class RecipeManager {

    private final Bingo bingo;

    public RecipeManager(Bingo bingo) {
        this.bingo = bingo;
    }

    public ShapedRecipe composterOak() {
        ItemStack item = new ItemStack(Material.COMPOSTER);
        NamespacedKey key = new NamespacedKey(this.bingo, "composterOak");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("w w", "w w", "www");
        recipe.setIngredient('w', Material.OAK_SLAB);
        return recipe;
    }

    public ShapedRecipe composterSpruce() {
        ItemStack item = new ItemStack(Material.COMPOSTER);
        NamespacedKey key = new NamespacedKey(this.bingo, "composterSpruce");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("w w", "w w", "www");
        recipe.setIngredient('w', Material.SPRUCE_SLAB);
        return recipe;
    }

    public ShapedRecipe composterBirch() {
        ItemStack item = new ItemStack(Material.COMPOSTER);
        NamespacedKey key = new NamespacedKey(this.bingo, "composterBirch");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("w w", "w w", "www");
        recipe.setIngredient('w', Material.BIRCH_SLAB);
        return recipe;
    }

    public ShapedRecipe composterJungle() {
        ItemStack item = new ItemStack(Material.COMPOSTER);
        NamespacedKey key = new NamespacedKey(this.bingo, "composterJungle");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("w w", "w w", "www");
        recipe.setIngredient('w', Material.JUNGLE_SLAB);
        return recipe;
    }

    public ShapedRecipe composterDarkOak() {
        ItemStack item = new ItemStack(Material.COMPOSTER);
        NamespacedKey key = new NamespacedKey(this.bingo, "composterDarkOak");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("w w", "w w", "www");
        recipe.setIngredient('w', Material.DARK_OAK_SLAB);
        return recipe;
    }

    public ShapedRecipe composterAcacia() {
        ItemStack item = new ItemStack(Material.COMPOSTER);
        NamespacedKey key = new NamespacedKey(this.bingo, "composterAcacia");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("w w", "w w", "www");
        recipe.setIngredient('w', Material.ACACIA_SLAB);
        return recipe;
    }
}
