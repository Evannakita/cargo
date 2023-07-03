package evannakita.cargo.block;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import evannakita.cargo.ModBlocks;
import evannakita.cargo.block.enums.TrackShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;

public class TrackWithCouplerBlock extends HorizontalFacingBlock {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty LINKED = BooleanProperty.of("linked");

    public static final List<String> PATTERNS = Arrays.asList("~-0^^0-~", "~-0^^^0-~", "~-00^^00-~", "~-00^^^00-~", "~-00^^^^00-~", "~-000^^^000-~", "~-000^^^^000-~");
    private static final Predicate<BlockState> IS_COUPLER_PREDICATE = state -> state != null && state.isOf(ModBlocks.TRACK_WITH_COUPLER);

    private static VoxelShape trackShape = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    public static final VoxelShape NORTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(4.0, 8.0, 0.0, 11.0, 16.0, 13.0), trackShape);
    public static final VoxelShape EAST_SHAPE = VoxelShapes.union(Block.createCuboidShape(3.0, 8.0, 4.0, 16.0, 16.0, 11.0), trackShape);
    public static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(5.0, 8.0, 3.0, 12.0, 16.0, 16.0), trackShape);
    public static final VoxelShape WEST_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 8.0, 5.0, 13.0, 16.0, 12.0), trackShape);

    public TrackWithCouplerBlock(Settings settings) {
        super(settings);
    }

    protected BlockState updateStates(BlockState state, WorldAccess world, BlockPos pos) {
        Direction facing = state.get(FACING);
        Boolean linked = false;
        Block northBlock = world.getBlockState(pos.north()).getBlock();
        Block eastBlock = world.getBlockState(pos.east()).getBlock();
        Block southBlock = world.getBlockState(pos.south()).getBlock();
        Block westBlock = world.getBlockState(pos.west()).getBlock();
        if (northBlock instanceof TrackWithUndercarriageBlock) {
            facing = Direction.NORTH;
            if (southBlock instanceof TrackWithUndercarriageBlock) {
                linked = true;
            }
        } else if (eastBlock instanceof TrackWithUndercarriageBlock) {
            facing = Direction.EAST;
            if (westBlock instanceof TrackWithUndercarriageBlock) {
                linked = true;
            }
        } else if (southBlock instanceof TrackWithUndercarriageBlock) {
            facing = Direction.SOUTH;
        } else if (westBlock instanceof TrackWithUndercarriageBlock) {
            facing = Direction.WEST;
        }
        return state.with(FACING, facing).with(LINKED, linked);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        Direction facing = state.get(FACING);
        if (oldState.isOf(ModBlocks.TRAIN_TRACKS)) {
            facing = Direction.NORTH;
            if (oldState.get(AbstractTrackBlock.TRACK_SHAPE) == TrackShape.EAST_WEST) {
                facing = Direction.EAST;
            }
        }
        world.setBlockState(pos, this.updateStates(state.with(FACING, facing), world, pos));
        this.trySpawnEntity(world, pos);
    }
    
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborstate, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return this.updateStates(state, world, pos);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        switch(state.get(FACING)) {
            case NORTH, SOUTH:
                world.setBlockState(pos, ModBlocks.TRAIN_TRACKS.getDefaultState().with(AbstractTrackBlock.TRACK_SHAPE, TrackShape.NORTH_SOUTH), NOTIFY_ALL);
                break;
            case EAST, WEST:
                world.setBlockState(pos, ModBlocks.TRAIN_TRACKS.getDefaultState().with(AbstractTrackBlock.TRACK_SHAPE, TrackShape.EAST_WEST), NOTIFY_ALL);
                break;
            default:
        }
    }

    private void trySpawnEntity(World world, BlockPos pos) {
        BlockPattern.Result result = null;
        for (int i = 0; i < 7; i++) {
            BlockPattern blockPattern = this.getPattern(i);
            result = blockPattern.searchAround(world, pos);
            if (result != null) {
                TrackWithCouplerBlock.spawnEntity(world, blockPattern, result, pos);
                break;
            }
        }
    }

    private static void spawnEntity(World world, BlockPattern blockPattern, BlockPattern.Result patternResult, BlockPos pos) {
        TrackWithCouplerBlock.breakPatternBlocks(world, patternResult);
        // FlatbedEntity entity = new FlatbedEntity((EntityType<FlatbedEntity>)Cargo.FLATBED, world);
        // double x1 = entity.getX();
        // double y1 = entity.getY();
        // double z1 = entity.getZ();
        // double depth = blockPattern.getDepth();
        // double height = blockPattern.getHeight();
        // double width = blockPattern.getWidth();
        // switch (patternResult.getForwards()) {
        //     case NORTH, SOUTH: {
        //         entity.setBoundingBox(new Box(x1 - depth/2, y1 - height/2, z1 - width/2, x1 + depth/2, y1 + height/2, z1 + width/2));
        //         break;
        //     }
        //     case EAST, WEST: {
        //         entity.setBoundingBox(new Box(x1 - width/2, y1 - height/2, z1 - depth/2, x1 + width/2, y1 + height/2, z1 + depth/2));
        //         break;
        //     }
        //     default:
        // }
        // entity.setBlockPattern(blockPattern);
        // world.spawnEntity(entity);
        // for (ServerPlayerEntity serverPlayerEntity : world.getNonSpectatingEntities(ServerPlayerEntity.class, entity.getBoundingBox().expand(5.0))) {
        //     Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity, entity);
        // }
        TrackWithCouplerBlock.updatePatternBlocks(world, patternResult);
    }

    public static void breakPatternBlocks(World world, BlockPattern.Result patternResult) {
        for (int i = 0; i < patternResult.getWidth(); ++i) {
            for (int j = 0; j < patternResult.getHeight(); ++j) {
                for (int k = 0; k < patternResult.getDepth(); ++k) {
                    CachedBlockPosition cachedBlockPosition = patternResult.translate(i, j, k);
                    BlockState blockState = cachedBlockPosition.getBlockState();
                    if (blockState.getBlock() instanceof TrackWithCouplerBlock || blockState.getBlock() instanceof TrackWithUndercarriageBlock) {
                        world.setBlockState(cachedBlockPosition.getBlockPos(), (ModBlocks.TRAIN_TRACKS.getDefaultState()), Block.NOTIFY_LISTENERS);
                    } else {
                        world.breakBlock(cachedBlockPosition.getBlockPos(), false);
                        world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, cachedBlockPosition.getBlockPos(), Block.getRawIdFromState(cachedBlockPosition.getBlockState()));
                    }
                }
            }
        }
    }

    public static void updatePatternBlocks(World world, BlockPattern.Result patternResult) {
        for (int i = 0; i < patternResult.getWidth(); ++i) {
            for (int j = 0; j < patternResult.getHeight(); ++j) {
                for (int k = 0; k < patternResult.getDepth(); ++k) {
                    CachedBlockPosition cachedBlockPosition = patternResult.translate(i, 0, k);
                    world.updateNeighbors(cachedBlockPosition.getBlockPos(), Blocks.AIR);
                }
            }
        }
    }

    private BlockPattern getPattern(int i) {
        BlockPattern pattern = BlockPatternBuilder.start()
            .aisle(PATTERNS.get(i))
            .where('-', (CachedBlockPosition.matchesBlockState(IS_COUPLER_PREDICATE)))
            .where('0', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(ModBlocks.TRACK_WITH_WHEELS)))
            .where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(ModBlocks.TRACK_WITH_UNDERCARRIAGE)))
            .where('~', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.AIR)))
            .build();
        return pattern;
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch(state.get(FACING)) {
            case NORTH:
                return NORTH_SHAPE;
            case EAST:
                return EAST_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
            default:
                return VoxelShapes.fullCube();
        }
    }

    public Property<Direction> getFacingProperty() {
        return FACING;
    }

    public Property<Boolean> getLinkedProperty() {
        return LINKED;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LINKED);
    }

}