package evannakita.cargo.screen;

import evannakita.cargo.Cargo;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;

public class RefineryScreenHandler extends AbstractFurnaceScreenHandler {
    public RefineryScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(Cargo.REFINERY_SCREEN_HANDLER, Cargo.REFINING_RECIPE, RecipeBookCategory.FURNACE, syncId, playerInventory);
    }

    public RefineryScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(Cargo.REFINERY_SCREEN_HANDLER, Cargo.REFINING_RECIPE, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
    }
}
