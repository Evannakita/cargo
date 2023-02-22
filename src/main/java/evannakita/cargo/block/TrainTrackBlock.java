package evannakita.cargo.block;

import evannakita.cargo.block.enums.TrackShape;
import net.minecraft.block.BlockState;

public class TrainTrackBlock extends AbstractTrackBlock {

    public TrainTrackBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(SHAPE, TrackShape.NORTH_SOUTH)).with(WATERLOGGED, false));
    }
}