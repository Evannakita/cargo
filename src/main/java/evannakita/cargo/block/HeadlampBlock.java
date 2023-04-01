package evannakita.cargo.block;

import evannakita.cargo.Cargo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class HeadlampBlock extends HorizontalFacingBlock {
    public static final BooleanProperty OFFSET = BooleanProperty.of("offset");
    public static final BooleanProperty POWERED = BooleanProperty.of("powered");

    public HeadlampBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        World world = context.getWorld();
        BlockState target = world.getBlockState(new BlockPos(context.getHitPos()));
        Direction facing = context.getSide().getOpposite();
        switch (facing) {
            case UP, DOWN:
                facing = context.getPlayerFacing();
            default:
        }
        boolean offset = false;
        if (target.isOf(Cargo.TRAIN_STRUCTURE_BLOCK)) {
            offset = (target.get(TrainStructureBlock.LEVEL) == 2);
        }
        return this.getDefaultState()
            .with(FACING, facing)
            .with(OFFSET, offset)
            .with(POWERED, world.isReceivingRedstonePower(context.getBlockPos()));
    }

    public Property<Direction> getFacingProperty() {
        return FACING;
    }
    
    public Property<Boolean> getOffsetProperty() {
        return OFFSET;
    }
    
    public Property<Boolean> getPoweredProperty() {
        return POWERED;
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OFFSET, POWERED);
    }
}
