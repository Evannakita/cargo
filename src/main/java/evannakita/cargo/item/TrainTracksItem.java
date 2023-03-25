package evannakita.cargo.item;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TrainTracksItem extends BlockItem {
    public TrainTracksItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    @Nullable
    public ItemPlacementContext getPlacementContext(ItemPlacementContext context) {
        BlockPos blockPos = context.getBlockPos();
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isOf(this.getBlock()) && (context.getSide() == Direction.UP)) {
            int i = (int)MathHelper.floor((MathHelper.wrapDegrees(context.getPlayerYaw() - 180.0f) + 22.5f) / 45.0f);
            switch (i % 2) {
                case 0: {
                    Direction direction = context.getPlayerFacing();
                    BlockPos.Mutable mutable = blockPos.mutableCopy().move(direction);
                    for (int j = 0;j < 15; j++) {
                        blockState = world.getBlockState(mutable);
                        if (!blockState.isOf(this.getBlock())) {
                            if (!blockState.canReplace(context)) break;
                            return ItemPlacementContext.offset(context, mutable, direction);
                        }
                        mutable.move(direction);
                        if (!direction.getAxis().isHorizontal()) continue;
                    }
                    return null;
                }
                case 1: {
                    Direction direction1;
                    Direction direction2;
                    switch (i) {
                        case 1: {
                            direction1 = Direction.NORTH;
                            direction2 = Direction.EAST;
                            break;
                        }
                        case 3: {
                            direction1 = Direction.SOUTH;
                            direction2 = Direction.EAST;
                            break;
                        }
                        case 5: {
                            direction1 = Direction.SOUTH;
                            direction2 = Direction.WEST;
                            break;
                        }
                        case 7: {
                            direction1 = Direction.NORTH;
                            direction2 = Direction.WEST;
                            break;
                        }
                        default: {
                            direction1 = Direction.UP;
                            direction2 = Direction.DOWN;
                        }
                    }
                    BlockPos.Mutable mutable = blockPos.mutableCopy().move(direction1).move(direction2);
                    for (int j = 0; j < 15; j++) {
                        blockState = world.getBlockState(mutable);
                        if (!blockState.isOf(this.getBlock())) {
                            if (!blockState.canReplace(context)) break;
                            return new ItemPlacementContext(
                                context.getWorld(),
                                context.getPlayer(),
                                context.getHand(),
                                context.getStack(),
                                new BlockHitResult(
                                    new Vec3d(
                                        (double)mutable.getX() + 0.5 + (double)direction1.getOffsetX() * 0.5 + (double)direction2.getOffsetX() * 0.5,
                                        (double)mutable.getY() + 0.5 + (double)direction1.getOffsetY() * 0.5 + (double)direction2.getOffsetY() * 0.5,
                                        (double)mutable.getZ() + 0.5 + (double)direction1.getOffsetZ() * 0.5 + (double)direction2.getOffsetZ() * 0.5
                                    ),
                                    Direction.UP,
                                    mutable,
                                    false
                                )
                            );
                        }
                        mutable.move(direction1).move(direction2);
                    }
                    return null;
                }
            }
        }
        return context;
    }
}
