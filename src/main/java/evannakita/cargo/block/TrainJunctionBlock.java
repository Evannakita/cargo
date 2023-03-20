package evannakita.cargo.block;

import evannakita.cargo.block.enums.JunctionShape;
import evannakita.cargo.block.enums.TrackShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TrainJunctionBlock extends AbstractTrackBlock {
    public static final EnumProperty<JunctionShape> JUNCTION_SHAPE = EnumProperty.of("junction_shape", JunctionShape.class);

    public TrainJunctionBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(JUNCTION_SHAPE, JunctionShape.T)).with(WATERLOGGED, false));
    }

    @Override
    protected BlockState updateBlockState(World world, BlockPos pos, BlockState state, boolean forceUpdate) {
        if (world.isClient) {
            return state;
        }
        JunctionShape junctionShape = state.get(this.getJunctionShapeProperty());
        TrackPlacementHelper placementHelper = new TrackPlacementHelper(world, pos, state);
        placementHelper.updateTrackState(world.isReceivingRedstonePower(pos), forceUpdate, junctionShape);
        return placementHelper.getBlockState();
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        FluidState fluidState = context.getWorld().getFluidState(context.getBlockPos());
        boolean bl = fluidState.getFluid() == Fluids.WATER;
        BlockState blockState = super.getDefaultState();
        float f = (float)MathHelper.floor((MathHelper.wrapDegrees(context.getPlayerYaw() - 180.0f) + 22.5f) / 45.0f) * 45.0f;
        JunctionShape junctionShape = JunctionShape.T;
        if (f == -135.0f || f == -45.0f || f == 45.0f || f == 135.0f) {
            junctionShape = JunctionShape.X;
        };
        return (BlockState)((BlockState)blockState.with(this.getJunctionShapeProperty(), junctionShape)).with(WATERLOGGED, bl);
    }
    @Override
    public Property<TrackShape> getTrackShapeProperty() {
        return null;
    }

    @Override
    public Property<JunctionShape> getJunctionShapeProperty() {
        return JUNCTION_SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return FLAT_SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(JUNCTION_SHAPE, WATERLOGGED);
    }
}