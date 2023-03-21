package evannakita.cargo.block;

import evannakita.cargo.Cargo;
import evannakita.cargo.block.enums.TrackShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class TrackWithUndercarriageBlock extends HorizontalFacingBlock {
    public static final BooleanProperty WIDTH = BooleanProperty.of("width");

    private static VoxelShape trackShape = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    private static VoxelShape undercarriageShape = Block.createCuboidShape(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);
    public static final VoxelShape SHAPE = VoxelShapes.union(trackShape, undercarriageShape);

    public TrackWithUndercarriageBlock(Settings settings) {
        super(settings);
    }
    
    protected BlockState updateFacingState(BlockState state, WorldAccess world, BlockPos pos) {
        Direction facing = state.get(FACING);
        if (world.getBlockState(pos.north()).isOf(Cargo.TRACK_WITH_UNDERCARRIAGE)) {
            facing = Direction.NORTH;
        }
        if (world.getBlockState(pos.east()).isOf(Cargo.TRACK_WITH_UNDERCARRIAGE)) {
            facing = Direction.EAST;
        }
        if (world.getBlockState(pos.south()).isOf(Cargo.TRACK_WITH_UNDERCARRIAGE)) {
            facing = Direction.SOUTH;
        }
        if (world.getBlockState(pos.west()).isOf(Cargo.TRACK_WITH_UNDERCARRIAGE)) {
            facing = Direction.WEST;
        }
        if (world.getBlockState(pos.north()).isOf(Cargo.TRACK_WITH_WHEELS)) {
            facing = Direction.NORTH;
        }
        if (world.getBlockState(pos.east()).isOf(Cargo.TRACK_WITH_WHEELS)) {
            facing = Direction.EAST;
        }
        if (world.getBlockState(pos.south()).isOf(Cargo.TRACK_WITH_WHEELS)) {
            facing = Direction.SOUTH;
        }
        if (world.getBlockState(pos.west()).isOf(Cargo.TRACK_WITH_WHEELS)) {
            facing = Direction.WEST;
        }
        return state.with(FACING, facing);
    }

    protected BlockState updateConnectionsState(BlockState state, WorldAccess world, BlockPos pos) {
        return state;
    }

    protected BlockState updateVisibilityState(BlockState state, WorldAccess world, BlockPos pos) {
        switch (state.get(FACING)) {
            case NORTH, SOUTH: {
                if (world.getBlockState(pos.up().east()).isOf(Cargo.TANK_CAR_HULL) || world.getBlockState(pos.up().west()).isOf(Cargo.TANK_CAR_HULL)) {
                    return state.with(WIDTH, false);
                } else {
                    return state.with(WIDTH, true);
                }
            }
            case EAST, WEST: {
                if (world.getBlockState(pos.up().north()).isOf(Cargo.TANK_CAR_HULL) || world.getBlockState(pos.up().south()).isOf(Cargo.TANK_CAR_HULL)) {
                    return state.with(WIDTH, false);
                } else {
                    return state.with(WIDTH, true);
                }
            }
            default:
                return state;
        }
    }

    protected void createStructureBlocks(BlockState state, WorldAccess world, BlockPos pos) {
        BlockState structureState = Cargo.TRAIN_STRUCTURE_BLOCK.getDefaultState();
        Direction facing = state.get(FACING);
        if (state.get(WIDTH)) {
            switch (facing) {
                case NORTH, SOUTH: {
                    if (world.getBlockState(pos.east()).isReplaceable()) {
                        world.setBlockState(pos.east(), structureState.with(TrainStructureBlock.LEVEL, 0).with(TrainStructureBlock.FACING, Direction.WEST), NOTIFY_ALL);
                    }
                    if (world.getBlockState(pos.west()).isReplaceable()) {
                        world.setBlockState(pos.west(), structureState.with(TrainStructureBlock.LEVEL, 0).with(TrainStructureBlock.FACING, Direction.EAST), NOTIFY_ALL);
                    }
                }
                case EAST, WEST: {
                    if (world.getBlockState(pos.north()).isReplaceable()) {
                        world.setBlockState(pos.north(), structureState.with(TrainStructureBlock.LEVEL, 0).with(TrainStructureBlock.FACING, Direction.SOUTH), NOTIFY_ALL);
                    }
                    if (world.getBlockState(pos.south()).isReplaceable()) {
                        world.setBlockState(pos.south(), structureState.with(TrainStructureBlock.LEVEL, 0).with(TrainStructureBlock.FACING, Direction.NORTH), NOTIFY_ALL);
                    }
                }
                default:
            }
        }
        Direction perpendicular = state.get(this.getFacingProperty()).rotateYClockwise();
        if (world.getBlockState(pos.up()).isAir()) {
            world.setBlockState(pos.up(), structureState.with(TrainStructureBlock.LEVEL, 1).with(TrainStructureBlock.FACING, perpendicular), NOTIFY_ALL);
        }
        if (world.getBlockState(pos.up(2)).isAir()) {
            world.setBlockState(pos.up(2), structureState.with(TrainStructureBlock.LEVEL, 2).with(TrainStructureBlock.FACING, perpendicular), NOTIFY_ALL);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        Direction facing = state.get(FACING);
        if (oldState.isOf(Cargo.TRAIN_TRACKS)) {
            facing = Direction.NORTH;
            if (oldState.get(AbstractTrackBlock.TRACK_SHAPE) == TrackShape.EAST_WEST) {
                facing = Direction.EAST;
            }
        }
        state = state.with(FACING, facing);
        state = this.updateFacingState(state, world, pos);
        state = this.updateConnectionsState(state, world, pos);
        state = this.updateVisibilityState(state, world, pos);
        world.setBlockState(pos, state);
        this.createStructureBlocks(state, world, pos);
    }
    
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborstate, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return this.updateFacingState(state, world, pos);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        switch(state.get(FACING)) {
            case NORTH, SOUTH:
                world.setBlockState(pos, Cargo.TRAIN_TRACKS.getDefaultState().with(AbstractTrackBlock.TRACK_SHAPE, TrackShape.NORTH_SOUTH), NOTIFY_ALL);
            case EAST, WEST:
                world.setBlockState(pos, Cargo.TRAIN_TRACKS.getDefaultState().with(AbstractTrackBlock.TRACK_SHAPE, TrackShape.EAST_WEST), NOTIFY_ALL);
            default:
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public Property<Direction> getFacingProperty() {
        return FACING;
    }

    public Property<Boolean> getWIDTHProperty() {
        return WIDTH;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WIDTH);
    }
}