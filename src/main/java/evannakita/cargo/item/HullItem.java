package evannakita.cargo.item;

import evannakita.cargo.block.TrainStructureBlock;
import net.minecraft.block.Block;
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
        if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof TrainStructureBlock && context.getSide().getAxis().isHorizontal()) {
            ActionResult actionResult = this.place(new ItemPlacementContext(context));
            return actionResult;
        }
        return ActionResult.FAIL;
    }
}
