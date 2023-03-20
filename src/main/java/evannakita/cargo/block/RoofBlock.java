package evannakita.cargo.block;

import evannakita.cargo.Cargo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class RoofBlock extends HorizontalFacingBlock {
    public static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);

    public RoofBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos pos = new BlockPos(context.getHitPos());
        BlockState placedOn = context.getWorld().getBlockState(pos);
        if (placedOn.isOf(Cargo.TRAIN_STRUCTURE_BLOCK)) {
            if (placedOn.get(TrainStructureBlock.LEVEL) > 0) {
                return this.getDefaultState().with(FACING, placedOn.get(FACING));
            }
        }
        return this.getDefaultState().with(FACING, context.getPlayerFacing());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public Property<Direction> getFacingProperty() {
        return FACING;
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
