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
        if (itemStack.isOf(Cargo.TRAIN_AXLE)) {
            if (!world.isClient) {
                switch (state.get(this.getTrackShapeProperty())) {
                    case NORTH_SOUTH: {
                        world.setBlockState(pos, (BlockState)Cargo.TRAIN_AXLE_BLOCK.getDefaultState().with(TrainAxleBlock.FACING, Direction.NORTH), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                        world.playSound(null, pos, SoundEvents.BLOCK_DEEPSLATE_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        if (!player.isCreative()) {
                            itemStack.decrement(1);
                        }
                    }
                    case EAST_WEST: {
                        world.setBlockState(pos, (BlockState)Cargo.TRAIN_AXLE_BLOCK.getDefaultState().with(TrainAxleBlock.FACING, Direction.EAST), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                        world.playSound(null, pos, SoundEvents.BLOCK_DEEPSLATE_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        if (!player.isCreative()) {
                            itemStack.decrement(1);
                        }
                    }
                    default: {
                        return ActionResult.PASS;
                    }
                }
            }
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }
}