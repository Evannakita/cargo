package evannakita.cargo.block;

import evannakita.cargo.Cargo;
import evannakita.cargo.block.enums.TrackShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TrainTrackBlock extends AbstractTrackBlock {

    public TrainTrackBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(SHAPE, TrackShape.NORTH_SOUTH)).with(WATERLOGGED, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Cargo.TRAIN_WHEELS)) {
            if (!world.isClient) {
                switch (state.get(this.getTrackShapeProperty())) {
                    default: {
                        return ActionResult.PASS;
                    }
                    case NORTH_SOUTH: {
                        BlockState newState = Cargo.TRACK_WITH_WHEELS.getDefaultState().with(TrackWithWheelsBlock.FACING, Direction.NORTH);
                        world.setBlockState(pos, newState, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                        ((TrackWithUndercarriageBlock)world.getBlockState(pos).getBlock()).updateStructureBlocks(newState, world, pos);
                        world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.75f, 1.0f);
                        if (!player.isCreative()) {
                            itemStack.decrement(1);
                        }
                    }
                    case EAST_WEST: {
                        BlockState newState = Cargo.TRACK_WITH_WHEELS.getDefaultState().with(TrackWithWheelsBlock.FACING, Direction.EAST);
                        world.setBlockState(pos, newState, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                        ((TrackWithUndercarriageBlock)world.getBlockState(pos).getBlock()).updateStructureBlocks(newState, world, pos);
                        world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.75f, 1.0f);
                        if (!player.isCreative()) {
                            itemStack.decrement(1);
                        }
                    }
                }
            }
            return ActionResult.success(world.isClient);
        }
        if (itemStack.isOf(Cargo.TRAIN_COUPLER)) {
            if (!world.isClient) {
                switch (state.get(this.getTrackShapeProperty())) {
                    default: {
                        return ActionResult.PASS;
                    }
                    case NORTH_SOUTH: {
                        BlockState newState = Cargo.TRACK_WITH_COUPLER.getDefaultState().with(TrackWithCouplerBlock.FACING, Direction.NORTH);
                        world.setBlockState(pos, newState, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                        world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.75f, 1.0f);
                        if (!player.isCreative()) {
                            itemStack.decrement(1);
                        }
                    }
                    case EAST_WEST: {
                        BlockState newState = Cargo.TRACK_WITH_COUPLER.getDefaultState().with(TrackWithCouplerBlock.FACING, Direction.EAST);
                        world.setBlockState(pos, newState, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                        world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.75f, 1.0f);
                        if (!player.isCreative()) {
                            itemStack.decrement(1);
                        }
                    }
                }
            }
            return ActionResult.success(world.isClient);
        }
        if (itemStack.isOf(Cargo.TRAIN_UNDERCARRIAGE)) {
            if (!world.isClient) {
                switch (state.get(this.getTrackShapeProperty())) {
                    default: {
                        return ActionResult.PASS;
                    }
                    case NORTH_SOUTH: {
                        BlockState newState = Cargo.TRACK_WITH_UNDERCARRIAGE.getDefaultState().with(TrackWithUndercarriageBlock.FACING, Direction.NORTH);
                        world.setBlockState(pos, newState, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                        ((TrackWithUndercarriageBlock)world.getBlockState(pos).getBlock()).updateStructureBlocks(newState, world, pos);
                        world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.75f, 1.0f);
                        if (!player.isCreative()) {
                            itemStack.decrement(1);
                        }
                    }
                    case EAST_WEST: {
                        BlockState newState = Cargo.TRACK_WITH_UNDERCARRIAGE.getDefaultState().with(TrackWithUndercarriageBlock.FACING, Direction.EAST);
                        world.setBlockState(pos, newState, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                        ((TrackWithUndercarriageBlock)world.getBlockState(pos).getBlock()).updateStructureBlocks(newState, world, pos);
                        world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.75f, 1.0f);
                        if (!player.isCreative()) {
                            itemStack.decrement(1);
                        }
                    }
                }
            }
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }
}