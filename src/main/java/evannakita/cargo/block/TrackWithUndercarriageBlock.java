package evannakita.cargo.block;

import evannakita.cargo.Cargo;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class TrackWithUndercarriageBlock extends HorizontalFacingBlock {
    private static VoxelShape trackShape = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    private static VoxelShape undercarriageShape = Block.createCuboidShape(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);
    public static final VoxelShape SHAPE = VoxelShapes.union(trackShape, undercarriageShape);

    public TrackWithUndercarriageBlock(Settings settings) {
        super(settings);
    }
    
    protected BlockState updateStructureBlocks(BlockState state, WorldAccess world, BlockPos pos) {
        BlockState structureState = Cargo.TRAIN_STRUCTURE_BLOCK.getDefaultState().with(TrainStructureBlock.FACING, state.get(this.getFacingProperty()));
        if (world.getBlockState(pos.up()).getBlock() instanceof AirBlock) {
            world.setBlockState(pos.up(), structureState.with(TrainStructureBlock.LEVEL, 1), Block.NOTIFY_LISTENERS);
        }
        if (world.getBlockState(pos.up(2)).getBlock() instanceof AirBlock) {
            world.setBlockState(pos.up(2), structureState.with(TrainStructureBlock.LEVEL, 2), Block.NOTIFY_LISTENERS);
        }
        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborstate, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return this.updateStructureBlocks(state, world, pos);
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