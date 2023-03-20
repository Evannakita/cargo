package evannakita.cargo.item;

import evannakita.cargo.block.TrainStructureBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class HullItem extends BlockItem {

    public HullItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockState placedOn = context.getWorld().getBlockState(context.getBlockPos());
        if (placedOn.getBlock() instanceof TrainStructureBlock) {
            if (placedOn.get(TrainStructureBlock.LEVEL) > 0) {
                if (context.getSide().getAxis() == placedOn.get(TrainStructureBlock.FACING).getAxis()) {
                    return this.place(new ItemPlacementContext(context));
                } else {
                    return ActionResult.FAIL;
                }
            }
        }
        return this.place(new ItemPlacementContext(context));
    }
}
