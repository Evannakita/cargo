package evannakita.cargo.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class TrackWithWheelsBlock extends TrackWithUndercarriageBlock {
    public static final IntProperty CONNECTIONS = IntProperty.of("connections", 0, 2);

    private static VoxelShape trackShape = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    private static VoxelShape undercarriageShape = Block.createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0);
    private static VoxelShape axleShapeNS = Block.createCuboidShape(0.0, 5.0, 6.0, 16.0, 9.0, 10.0);
    private static VoxelShape axleShapeEW = Block.createCuboidShape(6.0, 5.0, 0.0, 10.0, 9.0, 16.0);
    public static final VoxelShape NORTH_SOUTH_SHAPE = VoxelShapes.union(trackShape, undercarriageShape, axleShapeNS);
    public static final VoxelShape EAST_WEST_SHAPE = VoxelShapes.union(trackShape, undercarriageShape, axleShapeEW);

    public TrackWithWheelsBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch(state.get(FACING)) {
            case NORTH, SOUTH:
                return NORTH_SOUTH_SHAPE;
            case EAST, WEST:
                return EAST_WEST_SHAPE;
            default:
                return VoxelShapes.fullCube();
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborstate, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return this.updateConnectionsState(this.updateFacingState(state, world, pos), world, pos);
    }

    @Override
    protected BlockState updateConnectionsState(BlockState state, WorldAccess world, BlockPos pos) {
        int connections = 0;
        Direction direction = state.get(this.getFacingProperty());
        switch(direction) {
            case NORTH, SOUTH: {
                if (world.getBlockState(pos.south()).getBlock() instanceof TrackWithWheelsBlock) {
                    ++connections;
                }
                if (world.getBlockState(pos.north()).getBlock() instanceof TrackWithWheelsBlock) {
                    ++connections;
                }
            }
            case EAST, WEST: {
                if (world.getBlockState(pos.west()).getBlock() instanceof TrackWithWheelsBlock) {
                    ++connections;
                }
                if (world.getBlockState(pos.east()).getBlock() instanceof TrackWithWheelsBlock) {
                    ++connections;
                }
            }
            default:
        }
        return state.with(CONNECTIONS, connections);
    }

    public IntProperty getConnectionsProperty() {
        return CONNECTIONS;
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WIDTH, CONNECTIONS);
    }
}