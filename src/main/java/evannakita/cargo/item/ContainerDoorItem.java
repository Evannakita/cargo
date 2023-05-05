package evannakita.cargo.item;

import evannakita.cargo.ModBlocks;
import evannakita.cargo.block.ContainerBlock;
import evannakita.cargo.block.HullBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ContainerDoorItem extends BlockItem {

    public ContainerDoorItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        Vec3d hitPos = context.getHitPos();
        Direction targetSide = context.getSide();
        BlockPos targetPos = new BlockPos(hitPos);
        if (!context.hitsInsideBlock()) {
            switch (targetSide) {
                default:
                case SOUTH, EAST, UP:
                    targetPos = targetPos.offset(targetSide.getOpposite());
            }
        }
        BlockState target = world.getBlockState(targetPos);
        if (target.isOf(ModBlocks.CONTAINER)) {
            if (target.get(HullBlock.OFFSET)) {
                if (!target.get(ContainerBlock.DOOR)) {
                    world.setBlockState(targetPos, target.with(ContainerBlock.DOOR, true));
                    switch (target.get(HullBlock.LEVEL)) {
                        case 1: {
                            world.setBlockState(targetPos.up(), target.with(ContainerBlock.DOOR, true));
                            break;
                        }
                        case 2: {
                            world.setBlockState(targetPos.down(), target.with(ContainerBlock.DOOR, true));
                            break;
                        }
                    }
                    return ActionResult.success(world.isClient);
                }
            } else {
                return this.place(new ItemPlacementContext(context));
            }
        }
        return ActionResult.FAIL;
    }
}
