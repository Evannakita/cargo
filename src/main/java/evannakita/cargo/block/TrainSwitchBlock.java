package evannakita.cargo.block;

import evannakita.cargo.block.enums.SwitchShape;
import evannakita.cargo.block.enums.TrackShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TrainSwitchBlock extends AbstractTrackBlock {
    public static final EnumProperty<SwitchShape> SWITCH_SHAPE = EnumProperty.of("switch_shape", SwitchShape.class);
    public static final EnumProperty<SwitchDirection> SWITCH_DIRECTION = EnumProperty.of("switch_direction", SwitchDirection.class);

    public TrainSwitchBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
            this.stateManager.getDefaultState()
                .with(TRACK_SHAPE, TrackShape.NORTH_SOUTHEAST)
                .with(SWITCH_SHAPE, SwitchShape.NORTH_WYE)
                .with(SWITCH_DIRECTION, SwitchDirection.LEFT)
                .with(WATERLOGGED, false)
        );
    }

    @Override
    protected BlockState updateBlockState(World world, BlockPos pos, BlockState state, boolean forceUpdate) {
        if (world.isClient) {
            return state;
        }
        boolean powered = world.isReceivingRedstonePower(pos);
        SwitchShape switchShape = state.get(this.getSwitchShapeProperty());
        BlockState newState = new TrackPlacementHelper(world, pos, state).updateTrackState(powered, forceUpdate, switchShape);
        if (powered) {
            newState = newState
                .with(TRACK_SHAPE, newState.get(SWITCH_SHAPE).getTrackShape(powered))
                .with(SWITCH_DIRECTION, SwitchDirection.RIGHT);
        } else {
            newState = newState
                .with(TRACK_SHAPE, newState.get(SWITCH_SHAPE).getTrackShape(powered))
                .with(SWITCH_DIRECTION, SwitchDirection.LEFT);
        }
        world.setBlockState(pos, newState);
        if (state.get(SWITCH_DIRECTION) != newState.get(SWITCH_DIRECTION)) {
            world.playSound(null, pos, SoundEvents.BLOCK_LADDER_STEP, SoundCategory.BLOCKS, 0.5f, 1.0f);
        }
        return newState;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        FluidState fluidState = context.getWorld().getFluidState(context.getBlockPos());
        boolean bl = fluidState.getFluid() == Fluids.WATER;
        BlockState blockState = super.getDefaultState();
        float f = (float)MathHelper.floor((MathHelper.wrapDegrees(context.getPlayerYaw() - 180.0f) + 22.5f) / 45.0f) * 45.0f;
        SwitchShape switchShape = SwitchShape.SOUTH_WYE;
        if (f == 45.0f) {
            switchShape = SwitchShape.SOUTHWEST_WYE;
        };
        if (f == 90.0f) {
            switchShape = SwitchShape.WEST_WYE;
        };
        if (f == 135.0f) {
            switchShape = SwitchShape.NORTHWEST_WYE;
        };
        if (f == 180.0f) {
            switchShape = SwitchShape.NORTH_WYE;
        };
        if (f == -135.0f) {
            switchShape = SwitchShape.NORTHEAST_WYE;
        };
        if (f == -90.0f) {
            switchShape = SwitchShape.EAST_WYE;
        };
        if (f == -45.0f) {
            switchShape = SwitchShape.SOUTHEAST_WYE;
        };
        return (BlockState)((BlockState)blockState.with(this.getSwitchShapeProperty(), switchShape)).with(WATERLOGGED, bl);
    }

    @Override
    public Property<TrackShape> getTrackShapeProperty() {
        return TRACK_SHAPE;
    }

    @Override
    public Property<SwitchShape> getSwitchShapeProperty() {
        return SWITCH_SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return FLAT_SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TRACK_SHAPE, SWITCH_SHAPE, SWITCH_DIRECTION, WATERLOGGED);
    }

    public enum SwitchDirection implements StringIdentifiable {
        LEFT("left"),
        RIGHT("right");

        private final String name;

        private SwitchDirection(String name) {
            this.name = name;
        }
        @Override
        public String asString() {
            return this.name;
        }
    }
}