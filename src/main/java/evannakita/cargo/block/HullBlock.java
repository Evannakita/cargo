package evannakita.cargo.block;

import evannakita.cargo.Cargo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class HullBlock extends HorizontalFacingBlock {
    public static final BooleanProperty OFFSET = BooleanProperty.of("offset");

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 8.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    public HullBlock(Settings settings) {
        super(settings);
    }

    public Property<Direction> getFacingProperty() {
        return FACING;
    }
    
    public Property<Boolean> getOffsetProperty() {
        return OFFSET;
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos pos = new BlockPos(context.getHitPos());
        BlockState placedOn = context.getWorld().getBlockState(pos);
        if (placedOn.isOf(Cargo.TRAIN_STRUCTURE_BLOCK)) {
            if (placedOn.get(TrainStructureBlock.LEVEL) > 0) {
                return this.getDefaultState().with(FACING, context.getSide()).with(OFFSET, true);
            }
        }
        if (placedOn.getBlock() instanceof HullBlock || placedOn.isOf(Cargo.TRAIN_STRUCTURE_BLOCK)) {
            if (context.getWorld().getBlockState(pos.offset(placedOn.get(FACING))).isOf(Cargo.TRAIN_STRUCTURE_BLOCK)) {
                return this.getDefaultState().with(FACING, placedOn.get(FACING)).with(OFFSET, true);
            }
        }
        return this.getDefaultState().with(OFFSET, false);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (!state.get(OFFSET)) {
            return VoxelShapes.fullCube();
        }
        switch (state.get(FACING)) {
            default:
                return VoxelShapes.fullCube();
            case NORTH:
                return NORTH_SHAPE;
            case EAST:
                return EAST_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
        }
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OFFSET);
    }
}
