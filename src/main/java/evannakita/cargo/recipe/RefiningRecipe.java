package evannakita.cargo.recipe;

import evannakita.cargo.Cargo;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.util.Identifier;

public class RefiningRecipe extends AbstractCookingRecipe {
    public RefiningRecipe(Identifier id, String group, CookingRecipeCategory category,
            Ingredient input, ItemStack output, float experience, int cookTime) {
        super(Cargo.REFINING, id, group, category, input, output, experience, cookTime);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Cargo.FIREBOX);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Cargo.REFINING_SERIALIZER;
    }
}
