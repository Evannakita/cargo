package evannakita.cargo.screen;

import evannakita.cargo.Cargo;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;

public class FireboxScreenHandler extends AbstractFurnaceScreenHandler {
    public FireboxScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(Cargo.FIREBOX_SCREEN_HANDLER, Cargo.REFINING, RecipeBookCategory.FURNACE, syncId, playerInventory);
    }

    public FireboxScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(Cargo.FIREBOX_SCREEN_HANDLER, Cargo.REFINING, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
    }
}
