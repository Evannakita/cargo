package evannakita.cargo.block;

import java.util.function.Predicate;

import evannakita.cargo.Cargo;
import evannakita.cargo.entity.BogieEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.function.MaterialPredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class TrainAxleBlock extends HorizontalFacingBlock {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    private BlockPattern singleAxlePattern;
    private BlockPattern doubleAxlePattern;
    private BlockPattern tripleAxlePattern;
    private static final Predicate<BlockState> IS_BOGIE_AXLE_PREDICATE = state -> state != null && state.isOf(Cargo.TRAIN_AXLE_BLOCK);

    public TrainAxleBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (oldState.isOf(state.getBlock())) {
            return;
        }
        this.trySpawnEntity(world, pos);
    }

    private void trySpawnEntity(World world, BlockPos pos) {
        BlockPattern.Result result = this.getTripleAxlePattern().searchAround(world, pos);
        if (result != null) {
            BogieEntity bogieEntity = Cargo.BOGIE.create(world);
            if (bogieEntity != null) {
                TrainAxleBlock.spawnEntity(world, result, bogieEntity, result.translate(1, 0, 0).getBlockPos());
            }
            return;
        }
        result = this.getDoubleAxlePattern().searchAround(world, pos);
        if (result != null) {
            BogieEntity bogieEntity = Cargo.BOGIE.create(world);
            if (bogieEntity != null) {
                TrainAxleBlock.spawnEntity(world, result, bogieEntity, result.translate(1, 0, 0).getBlockPos());
            }
            return;
        }
        result = this.getSingleAxlePattern().searchAround(world, pos);
        if (result != null) {
            BogieEntity bogieEntity = Cargo.BOGIE.create(world);
            if (bogieEntity != null) {
                TrainAxleBlock.spawnEntity(world, result, bogieEntity, result.translate(1, 0, 0).getBlockPos());
            }
        }
    }

    private static void spawnEntity(World world, BlockPattern.Result patternResult, Entity entity, BlockPos pos) {
        TrainAxleBlock.breakPatternBlocks(world, patternResult);
        entity.refreshPositionAndAngles((double)pos.getX() + 0.5, (double)pos.getY() + 0.05, (double)pos.getZ() + 0.5, 0.0f, 0.0f);
        world.spawnEntity(entity);
        for (ServerPlayerEntity serverPlayerEntity : world.getNonSpectatingEntities(ServerPlayerEntity.class, entity.getBoundingBox().expand(5.0))) {
            Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity, entity);
        }
        TrainAxleBlock.updatePatternBlocks(world, patternResult);
    }

    public static void breakPatternBlocks(World world, BlockPattern.Result patternResult) {
        for (int i = 0; i < patternResult.getWidth(); ++i) {
            for (int k = 0; k < patternResult.getDepth(); ++k) {
                CachedBlockPosition cachedBlockPosition = patternResult.translate(i, 0, k);
                Block block = cachedBlockPosition.getBlockState().getBlock();
                if (block instanceof AbstractTrackBlock) {
                    continue;
                } else if (block instanceof TrainAxleBlock) {
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
    public BlockState getPlacementState(ItemPlacementContext context) {
        return (BlockState)this.getDefaultState().with(FACING, context.getPlayerFacing());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    private BlockPattern getSingleAxlePattern() {
        if (this.singleAxlePattern == null) {
            this.singleAxlePattern = BlockPatternBuilder.start()
                .aisle("~#~")
                .aisle("0^0")
                .aisle("~#~")
                .where('^', CachedBlockPosition.matchesBlockState(IS_BOGIE_AXLE_PREDICATE))
                .where('0', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Cargo.TRAIN_WHEEL)))
                .where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Cargo.TRAIN_TRACKS)))
                .where('~', CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR)))
                .build();
        }
        return this.singleAxlePattern;
    }

    private BlockPattern getDoubleAxlePattern() {
        if (this.doubleAxlePattern == null) {
            this.doubleAxlePattern = BlockPatternBuilder.start()
                .aisle("~#~")
                .aisle("0^0")
                .aisle("0^0")
                .aisle("~#~")
                .where('^', CachedBlockPosition.matchesBlockState(IS_BOGIE_AXLE_PREDICATE))
                .where('0', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Cargo.TRAIN_WHEEL)))
                .where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Cargo.TRAIN_TRACKS)))
                .where('~', CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR)))
                .build();
        }
        return this.doubleAxlePattern;
    }

    private BlockPattern getTripleAxlePattern() {
        if (this.tripleAxlePattern == null) {
            this.tripleAxlePattern = BlockPatternBuilder.start()
            .aisle("~#~")
            .aisle("0^0")
            .aisle("0^0")
            .aisle("0^0")
            .aisle("~#~")
            .where('^', CachedBlockPosition.matchesBlockState(IS_BOGIE_AXLE_PREDICATE))
            .where('0', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Cargo.TRAIN_WHEEL)))
            .where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Cargo.TRAIN_TRACKS)))
            .where('~', CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR)))
            .build();
        }
        return this.tripleAxlePattern;
    }
}