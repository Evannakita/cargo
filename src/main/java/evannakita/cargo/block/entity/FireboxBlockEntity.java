package evannakita.cargo.block.entity;

import evannakita.cargo.Cargo;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class FireboxBlockEntity extends AbstractFurnaceBlockEntity {
    public FireboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(Cargo.FIREBOX_ENTITY, blockPos, blockState, Cargo.REFINING);
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("cargo.firebox");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        // return new FireboxScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
        return null;
    }
}
