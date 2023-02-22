package evannakita.cargo.block.entity;

import evannakita.cargo.Cargo;
import evannakita.cargo.screen.RefineryScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class RefineryBlockEntity extends AbstractFurnaceBlockEntity {
    public RefineryBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(Cargo.REFINERY_ENTITY, blockPos, blockState, Cargo.REFINING_RECIPE);
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("cargo.refinery");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new RefineryScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

}
