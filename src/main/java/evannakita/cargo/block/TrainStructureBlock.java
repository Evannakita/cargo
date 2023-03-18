package evannakita.cargo.block;

import evannakita.cargo.Cargo;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class TrainStructureBlock extends HorizontalFacingBlock {
    protected static final VoxelShape NORTH_SOUTH_SHAPE = Block.createCuboidShape(7.99, 0.0, 0.0, 8.01, 16.0, 16.0);
    protected static final VoxelShape EAST_WEST_SHAPE = Block.createCuboidShape(0.0, 0.0, 7.99, 16.0, 16.0, 8.01);
    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 8.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 16.0, 16.0);
    protected static final IntProperty LEVEL = IntProperty.of("level", 1, 3);
    
    protected static int CONNECTIONS = 0;

    public TrainStructureBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborstate, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!(world.getBlockState(pos.down()).getBlock() instanceof TrackWithUndercarriageBlock) && !(world.getBlockState(pos.down(2)).getBlock() instanceof TrackWithUndercarriageBlock)) {
            world.setBlockState(pos, (Blocks.AIR.getDefaultState()), Block.NOTIFY_LISTENERS);
            return Blocks.AIR.getDefaultState();
        }
        if ((world.getBlockState(pos.down()).getBlock() instanceof TrackWithUndercarriageBlock) && (world.getBlockState(pos.up())).getBlock() instanceof AirBlock) {
            world.setBlockState(pos.up(), (Cargo.TRAIN_STRUCTURE_BLOCK.getDefaultState()), Block.NOTIFY_LISTENERS);
        }
        Block northBlock = world.getBlockState(pos.north()).getBlock();
        Block eastBlock = world.getBlockState(pos.east()).getBlock();
        Block southBlock = world.getBlockState(pos.south()).getBlock();
        Block westBlock = world.getBlockState(pos.west()).getBlock();
        if (!(northBlock instanceof HullBlock) && !(eastBlock instanceof HullBlock) && !(southBlock instanceof HullBlock) && !(westBlock instanceof HullBlock)) {
            CONNECTIONS = 0;
            BlockState baseState = world.getBlockState(pos.down(state.get(LEVEL)));
            if (baseState.getBlock() instanceof TrackWithUndercarriageBlock) {
                world.setBlockState(pos, state.with(FACING, baseState.get(TrackWithUndercarriageBlock.FACING)), Block.NOTIFY_LISTENERS);
            }
        } else {
            if (((northBlock instanceof HullBlock) && (southBlock instanceof HullBlock)) || ((eastBlock instanceof HullBlock) && (westBlock instanceof HullBlock))) {
                CONNECTIONS = 2;
            }
            if ((northBlock instanceof HullBlock) && !(southBlock instanceof HullBlock)) {
                CONNECTIONS = 1;
                world.setBlockState(pos, state.with(FACING, Direction.NORTH), Block.NOTIFY_LISTENERS);
            }
            if ((eastBlock instanceof HullBlock) && !(westBlock instanceof HullBlock)) {
                CONNECTIONS = 1;
                world.setBlockState(pos, state.with(FACING, Direction.EAST), Block.NOTIFY_LISTENERS);
            }
            if ((southBlock instanceof HullBlock) && !(northBlock instanceof HullBlock)) {
                CONNECTIONS = 1;
                world.setBlockState(pos, state.with(FACING, Direction.SOUTH), Block.NOTIFY_LISTENERS);
            }
            if ((westBlock instanceof HullBlock) && !(eastBlock instanceof HullBlock)) {
                CONNECTIONS = 1;
                world.setBlockState(pos, state.with(FACING, Direction.WEST), Block.NOTIFY_LISTENERS);
            }
        }
        return state;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (CONNECTIONS == 0) {
            switch (state.get(FACING)) {
                default:
                    return VoxelShapes.fullCube();
                case NORTH, SOUTH:
                    return NORTH_SOUTH_SHAPE;
                case EAST, WEST:
                    return EAST_WEST_SHAPE;
            }
        } else if (CONNECTIONS == 1) {
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
        } else {
            return VoxelShapes.fullCube();
        }
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (CONNECTIONS == 0) {
            return VoxelShapes.empty();
        }
        return this.getOutlineShape(state, world, pos, context);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    public Property<Direction> getFacingProperty() {
        return FACING;
    }

    public IntProperty getLevelProperty() {
        return LEVEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LEVEL);
    }
}
