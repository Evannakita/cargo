package evannakita.cargo.block;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import evannakita.cargo.Cargo;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class TrackWithWheelsBlock extends TrackWithUndercarriageBlock {
    public static final IntProperty CONNECTIONS = IntProperty.of("connections", 0, 2);
    public static final List<String> PATTERNS = Arrays.asList("-0^^0-", "-0^^^0-", "-0^^^^0-", "-00^^00-", "-00^^^00-", "-00^^^^00-", "-00^^^^^00-", "-00^^^^^^00-", "-000^^^^000-", "-000^^^^^000-", "-000^^^^^^^000-");

    private static final Predicate<BlockState> IS_BOGIE_AXLE_PREDICATE = state -> state != null && state.isOf(Cargo.TRACK_WITH_WHEELS);

    private static VoxelShape trackShapeNS = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    private static VoxelShape axleShapeNS = Block.createCuboidShape(0.0, 5.0, 6.0, 16.0, 9.0, 10.0);
    private static VoxelShape undercarriageShapeNS = Block.createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0);
    public static final VoxelShape NORTH_SOUTH_SHAPE = VoxelShapes.union(trackShapeNS, axleShapeNS, undercarriageShapeNS);

    public TrackWithWheelsBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return NORTH_SOUTH_SHAPE;
    }
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        this.trySpawnEntity(world, pos);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        this.trySpawnEntity(world, pos);
        this.checkConnections(state, world, pos);
    }

    private void checkConnections(BlockState state, World world, BlockPos pos) {
        int connections = 0;
        Direction direction = state.get(this.getFacingProperty());
        switch(direction) {
            case NORTH, SOUTH: {
                if (world.getBlockState(pos.south()).getBlock() instanceof TrackWithWheelsBlock) {
                    ++connections;
                    direction = Direction.SOUTH;
                }
                if (world.getBlockState(pos.north()).getBlock() instanceof TrackWithWheelsBlock) {
                    ++connections;
                    direction = Direction.NORTH;
                }
            }
            case EAST, WEST: {
                if (world.getBlockState(pos.west()).getBlock() instanceof TrackWithWheelsBlock) {
                    ++connections;
                    direction = Direction.WEST;
                }
                if (world.getBlockState(pos.east()).getBlock() instanceof TrackWithWheelsBlock) {
                    ++connections;
                    direction = Direction.EAST;
                }
            }
            default:
        }
        state = (BlockState)state.with(this.getFacingProperty(), direction).with(this.getConnectionsProperty(), connections);
    }

    public IntProperty getConnectionsProperty() {
        return CONNECTIONS;
    }

    private void trySpawnEntity(World world, BlockPos pos) {
        BlockPattern.Result result = null;
        for (int i = 0; i < 11; i++) {
            result = this.getPattern(i).searchAround(world, pos);
        }
        if (result != null) {
            TrackWithWheelsBlock.spawnEntity(world, result, null, pos);
        }
    }

    private static void spawnEntity(World world, BlockPattern.Result patternResult, Entity entity, BlockPos pos) {
        if (entity != null) {
            TrackWithWheelsBlock.breakPatternBlocks(world, patternResult);
            entity.refreshPositionAndAngles((double)pos.getX() + 0.5, (double)pos.getY() + 0.05, (double)pos.getZ() + 0.5, 0.0f, 0.0f);
            world.spawnEntity(entity);
            for (ServerPlayerEntity serverPlayerEntity : world.getNonSpectatingEntities(ServerPlayerEntity.class, entity.getBoundingBox().expand(5.0))) {
                Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity, entity);
            }
            TrackWithWheelsBlock.updatePatternBlocks(world, patternResult);
        }
    }

    public static void breakPatternBlocks(World world, BlockPattern.Result patternResult) {
        for (int i = 0; i < patternResult.getWidth(); ++i) {
            for (int k = 0; k < patternResult.getDepth(); ++k) {
                CachedBlockPosition cachedBlockPosition = patternResult.translate(i, 0, k);
                Block block = cachedBlockPosition.getBlockState().getBlock();
                if (block instanceof AbstractTrackBlock) {
                    continue;
                } else if (block instanceof TrackWithWheelsBlock) {
                    world.setBlockState(cachedBlockPosition.getBlockPos(), (Cargo.TRAIN_TRACKS.getDefaultState()), Block.NOTIFY_LISTENERS);
                } else {
                    world.setBlockState(cachedBlockPosition.getBlockPos(), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, cachedBlockPosition.getBlockPos(), Block.getRawIdFromState(cachedBlockPosition.getBlockState()));
                }
            }
        }
    }

    public static void updatePatternBlocks(World world, BlockPattern.Result patternResult) {
        for (int i = 0; i < patternResult.getWidth(); ++i) {
            for (int k = 0; k < patternResult.getDepth(); ++k) {
                CachedBlockPosition cachedBlockPosition = patternResult.translate(i, 0, k);
                world.updateNeighbors(cachedBlockPosition.getBlockPos(), Blocks.AIR);
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, CONNECTIONS);
    }

    private BlockPattern getPattern(int i) {
        BlockPattern pattern = BlockPatternBuilder.start()
            .aisle(PATTERNS.get(i))
            .where('0', (CachedBlockPosition.matchesBlockState(IS_BOGIE_AXLE_PREDICATE)))
            .where('-', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Cargo.TRACK_WITH_COUPLER)))
            .where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Cargo.TRACK_WITH_UNDERCARRIAGE)))
            .build();
        return pattern;
    }
}