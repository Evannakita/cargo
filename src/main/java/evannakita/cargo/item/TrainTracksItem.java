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
            int f = (int)MathHelper.floor((MathHelper.wrapDegrees(context.getPlayerYaw() - 180.0f) + 22.5f) / 45.0f);
            switch (f % 2) {
                case 0: {
                    Direction direction = context.getPlayerFacing();
                    int i = 0;
                    BlockPos.Mutable mutable = blockPos.mutableCopy().move(direction);
                    while (i < 15) {
                        blockState = world.getBlockState(mutable);
                        if (!blockState.isOf(this.getBlock())) {
                            if (!blockState.canReplace(context)) break;
                            return ItemPlacementContext.offset(context, mutable, direction);
                        }
                        mutable.move(direction);
                        if (!direction.getAxis().isHorizontal()) continue;
                        ++i;
                    }
                    return null;
                }
                case 1: {
                    Direction direction1;
                    Direction direction2;
                    switch (f) {
                        case 1: {
                            direction1 = Direction.NORTH;
                            direction2 = Direction.EAST;
                        }
                        case 3: {
                            direction1 = Direction.SOUTH;
                            direction2 = Direction.EAST;
                        }
                        case 5: {
                            direction1 = Direction.SOUTH;
                            direction2 = Direction.WEST;
                        }
                        case 7: {
                            direction1 = Direction.NORTH;
                            direction2 = Direction.WEST;
                        }
                        default: {
                            direction1 = Direction.UP;
                            direction2 = Direction.DOWN;
                        }
                    }
                    int i = 0;
                    BlockPos.Mutable mutable = blockPos.mutableCopy().move(direction1).move(direction2);
                    while (i < 15) {
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
                        ++i;
                    }
                    return null;
                }
            }
        }
        return context;
    }
}
