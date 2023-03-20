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
import net.minecraft.world.World;

public class TrainTrackBlock extends AbstractTrackBlock {

    public TrainTrackBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(TRACK_SHAPE, TrackShape.NORTH_SOUTH)).with(WATERLOGGED, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!world.isClient && (state.get(TRACK_SHAPE) == TrackShape.NORTH_SOUTH || state.get(TRACK_SHAPE) == TrackShape.EAST_WEST)) {
            if (itemStack.isOf(Cargo.TRAIN_COUPLER)) {
                world.setBlockState(pos, Cargo.TRACK_WITH_COUPLER.getDefaultState(), Block.NOTIFY_ALL);
                world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.75f, 1.0f);
                if (!player.isCreative()) {
                    itemStack.decrement(1);
                }
                return ActionResult.success(world.isClient);
            }
            if (itemStack.isOf(Cargo.TRAIN_UNDERCARRIAGE)) {
                world.setBlockState(pos, Cargo.TRACK_WITH_UNDERCARRIAGE.getDefaultState(), Block.NOTIFY_ALL);
                world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.75f, 1.0f);
                if (!player.isCreative()) {
                    itemStack.decrement(1);
                }
                return ActionResult.success(world.isClient);
            }
            if (itemStack.isOf(Cargo.TRAIN_WHEELS)) {
                world.setBlockState(pos, Cargo.TRACK_WITH_WHEELS.getDefaultState(), Block.NOTIFY_ALL);
                world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.75f, 1.0f);
                if (!player.isCreative()) {
                    itemStack.decrement(1);
                }
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }
}