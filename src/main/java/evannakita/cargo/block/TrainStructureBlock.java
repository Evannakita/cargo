package evannakita.cargo.block;

import evannakita.cargo.ModBlocks;
import evannakita.cargo.block.enums.TrackShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class TrainStructureBlock extends HorizontalFacingBlock {
    public static final IntProperty LEVEL = IntProperty.of("level", 0, 2);
    public static final IntProperty CONNECTIONS = IntProperty.of("connections", 0, 2);

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 8.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 16.0, 16.0);
    
    public TrainStructureBlock(Settings settings) {
        super(settings);
    }
    
    public BlockState updateBlockState(BlockState state, WorldAccess world, BlockPos pos) {
        if (state.get(LEVEL) == 0) {
            if (!(world.getBlockState(pos.offset(state.get(FACING))).getBlock() instanceof TrackWithUndercarriageBlock)) {
                return Blocks.AIR.getDefaultState();
            }
            return state.with(CONNECTIONS, 1);
        }
        BlockState baseState = world.getBlockState(pos.down(state.get(LEVEL)));
        if (!(baseState.getBlock() instanceof TrackWithUndercarriageBlock)) {
            world.removeBlock(pos, false);
            return Blocks.AIR.getDefaultState();
        } else if ((state.get(LEVEL) == 1) && (world.getBlockState(pos.up())).isAir()) {
            world.setBlockState(pos.up(), this.updateBlockState(state.with(LEVEL, 2), world, pos.up()), Block.NOTIFY_ALL);
        } else if ((state.get(LEVEL) == 2) && (world.getBlockState(pos.down())).isAir()) {
            world.setBlockState(pos.down(), this.updateBlockState(state.with(LEVEL, 2), world, pos.down()), Block.NOTIFY_ALL);
        }
        Block northBlock = world.getBlockState(pos.north()).getBlock();
        Block eastBlock = world.getBlockState(pos.east()).getBlock();
        Block southBlock = world.getBlockState(pos.south()).getBlock();
        Block westBlock = world.getBlockState(pos.west()).getBlock();
        if (!(northBlock instanceof HullBlock) && !(eastBlock instanceof HullBlock) && !(southBlock instanceof HullBlock) && !(westBlock instanceof HullBlock)) {
            state = state.with(CONNECTIONS, 0);
            if (baseState.getBlock() instanceof TrackWithUndercarriageBlock) {
                state = state.with(FACING, baseState.get(FACING).rotateYClockwise());
            }
        } else {
            if (((northBlock instanceof HullBlock) && (southBlock instanceof HullBlock)) || ((eastBlock instanceof HullBlock) && (westBlock instanceof HullBlock))) {
                state = state.with(CONNECTIONS, 2);
            } else {
                state = state.with(CONNECTIONS, 1);
                if (northBlock instanceof HullBlock) {
                    state = state.with(FACING, Direction.NORTH);
                }
                if (eastBlock instanceof HullBlock) {
                    state = state.with(FACING, Direction.EAST);
                }
                if (southBlock instanceof HullBlock) {
                    state = state.with(FACING, Direction.SOUTH);
                }
                if (westBlock instanceof HullBlock) {
                    state = state.with(FACING, Direction.WEST);
                }
            }
        }
        return state;
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        BlockState newState = updateBlockState(state, world, pos);
        if (state != newState) {
            world.setBlockState(pos, newState, NOTIFY_ALL);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborstate, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return updateBlockState(state, world, pos);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos targetPos = pos;
        if (state.get(CONNECTIONS) == 1) {
            targetPos = pos.offset(state.get(FACING));
        } else if(state.get(CONNECTIONS) == 2) {
            switch (state.get(FACING)) {
                default:
                case NORTH, SOUTH: {
                    if (pos.toCenterPos().getZ() < player.getZ()) {
                        targetPos = pos.offset(Direction.SOUTH);
                    } else {
                        targetPos = pos.offset(Direction.NORTH);
                    }
                    break;
                }
                case EAST, WEST: {
                    if (pos.toCenterPos().getX() < player.getX()) {
                        targetPos = pos.offset(Direction.EAST);
                    } else {
                        targetPos = pos.offset(Direction.WEST);
                    }
                    break;
                }
            }
        }
        BlockState targetState = world.getBlockState(targetPos);
        if (targetState.getBlock() instanceof HullBlock) {
            this.spawnBreakParticles(world, player, pos, targetState);
            world.removeBlock(targetPos, false);
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (state.get(LEVEL) == 0) {
            switch(state.get(FACING)) {
                case NORTH, SOUTH:
                    world.setBlockState(pos.offset(state.get(FACING)), ModBlocks.TRAIN_TRACKS.getDefaultState().with(AbstractTrackBlock.TRACK_SHAPE, TrackShape.EAST_WEST), NOTIFY_ALL);
                    break;
                case EAST, WEST:
                    world.setBlockState(pos.offset(state.get(FACING)), ModBlocks.TRAIN_TRACKS.getDefaultState().with(AbstractTrackBlock.TRACK_SHAPE, TrackShape.NORTH_SOUTH), NOTIFY_ALL);
                    break;
                default:
            }
        } else {
            world.setBlockState(pos, updateBlockState(state, world, pos), NOTIFY_LISTENERS);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(CONNECTIONS) == 0) {
            return VoxelShapes.empty();
        } else if (state.get(CONNECTIONS) == 1) {
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

    public IntProperty getConnectionsProperty() {
        return CONNECTIONS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LEVEL, CONNECTIONS);
    }
}
