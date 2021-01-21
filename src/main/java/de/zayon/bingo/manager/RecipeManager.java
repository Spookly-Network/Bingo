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

    public ShapedRecipe composter() {
        ItemStack item = new ItemStack(Material.COMPOSTER);
        NamespacedKey key = new NamespacedKey(this.bingo, "composter");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("w w", "w w", "www");
        recipe.setIngredient('w', Material.OAK_SLAB);
        recipe.setIngredient('w', Material.BIRCH_SLAB);
        recipe.setIngredient('w', Material.SPRUCE_SLAB);
        recipe.setIngredient('w', Material.JUNGLE_SLAB);
        recipe.setIngredient('w', Material.DARK_OAK_SLAB);
        recipe.setIngredient('w', Material.ACACIA_SLAB);

        return recipe;
    }
}
