package evannakita.cargo.block;

import evannakita.cargo.block.enums.JunctionShape;
import evannakita.cargo.block.enums.SwitchShape;
import evannakita.cargo.block.enums.TrackShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class AbstractTrackBlock extends Block implements Waterloggable {
    protected static final VoxelShape FLAT_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    protected static final VoxelShape BOTTOM_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
    protected static final VoxelShape MIDDLE_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0);
    protected static final VoxelShape TOP_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final EnumProperty<TrackShape> TRACK_SHAPE = EnumProperty.of("track_shape", TrackShape.class);

    public static boolean isTrack(World world, BlockPos pos) {
        return AbstractTrackBlock.isTrack(world.getBlockState(pos));
    }

    public static boolean isTrack(BlockState state) {
        return state.getBlock() instanceof AbstractTrackBlock;
    }

    public AbstractTrackBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (oldState.isOf(state.getBlock())) {
            return;
        }
        this.updateCurves(state, world, pos, notify);
    }

    protected BlockState updateCurves(BlockState state, World world, BlockPos pos, boolean notify) {
        state = this.updateBlockState(world, pos, state, true);
        return state;
    }

    protected BlockState updateBlockState(World world, BlockPos pos, BlockState state, boolean forceUpdate) {
        if (world.isClient) {
            return state;
        }
        TrackShape trackShape = state.get(this.getTrackShapeProperty());
        BlockState newState = new TrackPlacementHelper(world, pos, state).updateTrackState(world.isReceivingRedstonePower(pos), forceUpdate, trackShape);
        return newState;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        FluidState fluidState = context.getWorld().getFluidState(context.getBlockPos());
        boolean bl = fluidState.getFluid() == Fluids.WATER;
        BlockState blockState = super.getDefaultState();
        float f = (float)MathHelper.floor((MathHelper.wrapDegrees(context.getPlayerYaw() - 180.0f) + 22.5f) / 45.0f) * 45.0f;
        TrackShape trackShape = TrackShape.NORTH_SOUTH;
        if (f == 45.0f || f == -135.0f) {
            trackShape = TrackShape.NORTHEAST_SOUTHWEST;
        };
        if (f == 90.0f || f == -90.0f) {
            trackShape = TrackShape.EAST_WEST;
        };
        if (f == 135.0f || f == -45.0f) {
            trackShape = TrackShape.NORTHWEST_SOUTHEAST;
        };
        return (BlockState)((BlockState)blockState.with(this.getTrackShapeProperty(), trackShape)).with(WATERLOGGED, bl);
    }

    public Property<TrackShape> getTrackShapeProperty() {
        return TRACK_SHAPE;
    }

    public Property<SwitchShape> getSwitchShapeProperty() {
        return null;
    }

    public Property<JunctionShape> getJunctionShapeProperty() {
        return null;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        TrackShape trackShape;
        trackShape = state.isOf(this) ? state.get(this.getTrackShapeProperty()) : null;
        switch (trackShape) {
            case NORTH_BOTTOM, EAST_BOTTOM, SOUTH_BOTTOM, WEST_BOTTOM:
                return BOTTOM_SHAPE;
            case NORTH_MIDDLE, EAST_MIDDLE, SOUTH_MIDDLE, WEST_MIDDLE:
                return MIDDLE_SHAPE;
            case NORTH_TOP, EAST_TOP, SOUTH_TOP, WEST_TOP:
                return TOP_SHAPE;
            default:
                return FLAT_SHAPE;
        }
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180: {
                switch (state.get(TRACK_SHAPE)) {
                    case NORTH_SOUTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.SOUTH_NORTHWEST);
                    case NORTH_SOUTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.SOUTH_NORTHEAST);
                    case EAST_SOUTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.WEST_NORTHEAST);
                    case EAST_NORTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.WEST_SOUTHEAST);
                    case SOUTH_NORTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTH_SOUTHEAST);
                    case SOUTH_NORTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTH_SOUTHWEST);
                    case WEST_NORTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.EAST_SOUTHWEST);
                    case WEST_SOUTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.EAST_NORTHWEST);
                    default:
                        return state;
                }
            }
            case COUNTERCLOCKWISE_90: {
                switch (state.get(TRACK_SHAPE)) {
                    case NORTH_SOUTH:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.EAST_WEST);
                    case EAST_WEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTH_SOUTH);
                    case NORTHEAST_SOUTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTHWEST_SOUTHEAST);
                    case NORTHWEST_SOUTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTHEAST_SOUTHWEST);
                    case NORTH_SOUTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.WEST_NORTHEAST);
                    case NORTH_SOUTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.WEST_SOUTHEAST);
                    case EAST_SOUTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTH_SOUTHEAST);
                    case EAST_NORTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTH_SOUTHWEST);
                    case SOUTH_NORTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.EAST_SOUTHWEST);
                    case SOUTH_NORTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.EAST_NORTHWEST);
                    case WEST_NORTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.SOUTH_NORTHWEST);
                    case WEST_SOUTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.SOUTH_NORTHEAST);
                    default:
                        return state;
                }
            }
            case CLOCKWISE_90: {
                switch (state.get(TRACK_SHAPE)) {
                    case NORTH_SOUTH:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.EAST_WEST);
                    case EAST_WEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTH_SOUTH);
                    case NORTHEAST_SOUTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTHWEST_SOUTHEAST);
                    case NORTHWEST_SOUTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTHEAST_SOUTHWEST);
                    case NORTH_SOUTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.EAST_SOUTHWEST);
                    case NORTH_SOUTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.EAST_NORTHWEST);
                    case EAST_SOUTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.SOUTH_NORTHWEST);
                    case EAST_NORTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.SOUTH_NORTHEAST);
                    case SOUTH_NORTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.WEST_NORTHEAST);
                    case SOUTH_NORTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.WEST_SOUTHEAST);
                    case WEST_NORTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTH_SOUTHEAST);
                    case WEST_SOUTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTH_SOUTHWEST);
                    default:
                        return state;
                }
            }
            default:
                return state;
        }
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        TrackShape trackShape = state.get(TRACK_SHAPE);
        switch (mirror) {
            case LEFT_RIGHT: {
                switch (trackShape) {
                    case NORTHEAST_SOUTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTHWEST_SOUTHEAST);
                    case NORTHWEST_SOUTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTHEAST_SOUTHWEST);
                    case NORTH_SOUTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.SOUTH_NORTHEAST);
                    case NORTH_SOUTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.SOUTH_NORTHWEST);
                    case EAST_SOUTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.EAST_NORTHWEST);
                    case EAST_NORTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.EAST_SOUTHWEST);
                    case SOUTH_NORTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTH_SOUTHWEST);
                    case SOUTH_NORTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTH_SOUTHEAST);
                    case WEST_NORTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.WEST_SOUTHEAST);
                    case WEST_SOUTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.WEST_NORTHEAST);
                    default:
                        break;
                }
            }
            case FRONT_BACK: {
                switch (trackShape) {
                    case NORTHEAST_SOUTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTHWEST_SOUTHEAST);
                    case NORTHWEST_SOUTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTHEAST_SOUTHWEST);
                    case NORTH_SOUTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTH_SOUTHWEST);
                    case NORTH_SOUTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.NORTH_SOUTHEAST);
                    case EAST_SOUTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.WEST_SOUTHEAST);
                    case EAST_NORTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.WEST_NORTHEAST);
                    case SOUTH_NORTHWEST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.SOUTH_NORTHEAST);
                    case SOUTH_NORTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.SOUTH_NORTHWEST);
                    case WEST_NORTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.EAST_NORTHWEST);
                    case WEST_SOUTHEAST:
                        return (BlockState)state.with(TRACK_SHAPE, TrackShape.EAST_SOUTHWEST);
                    default:
                        break;
                }
            }
            default:
                return state;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TRACK_SHAPE, WATERLOGGED);
    }
}