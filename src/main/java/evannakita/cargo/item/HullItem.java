package evannakita.cargo.item;

import evannakita.cargo.ModBlocks;
import evannakita.cargo.block.TrackWithUndercarriageBlock;
import evannakita.cargo.block.TrainStructureBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class HullItem extends BlockItem {

    public HullItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos targetPos = new BlockPos(context.getBlockPos());
        Direction targetSide = context.getSide();
        BlockState target = world.getBlockState(targetPos);
        if (target.isOf(ModBlocks.TRAIN_STRUCTURE_BLOCK) || target.getBlock() instanceof TrackWithUndercarriageBlock) {
            int targetLevel = 0;
            if (target.isOf(ModBlocks.TRAIN_STRUCTURE_BLOCK)) {
                if (targetSide.getAxis() == target.get(Properties.HORIZONTAL_FACING).getAxis()) {
                    return this.place(new ItemPlacementContext(context));
                }
                targetLevel = target.get(TrainStructureBlock.LEVEL);
            }
            if ((targetLevel > 0) || (targetSide == Direction.UP)) {
                BlockPos newTargetPos = targetPos.offset(targetSide);
                BlockState newTarget = world.getBlockState(newTargetPos);
                if (newTarget.getBlock() instanceof TrainStructureBlock) {
                    Direction newTargetSide;
                    switch (newTarget.get(Properties.HORIZONTAL_FACING)) {
                        default:
                        case NORTH, SOUTH: {
                            if (newTargetPos.toCenterPos().getZ() < targetPos.getZ()) {
                                newTargetSide = Direction.SOUTH;
                            } else {
                                newTargetSide = Direction.NORTH;
                            }
                            break;
                        }
                        case EAST, WEST: {
                            if (newTargetPos.toCenterPos().getX() < targetPos.getX()) {
                                newTargetSide = Direction.EAST;
                            } else {
                                newTargetSide = Direction.WEST;
                            }
                            break;
                        }
                    }
                    return this.place(
                        new ItemPlacementContext(
                            world,
                            context.getPlayer(),
                            context.getHand(),
                            context.getStack(),
                            new BlockHitResult(
                                newTargetPos.toCenterPos(),
                                newTargetSide,
                                newTargetPos.offset(newTargetSide),
                                true
                            )
                        )
                    );
                } else {
                    return ActionResult.FAIL;
                }
            }
        }
        return this.place(new ItemPlacementContext(context));
    }
}
