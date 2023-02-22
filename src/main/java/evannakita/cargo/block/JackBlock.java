package evannakita.cargo.block;

import evannakita.cargo.block.entity.JackBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class JackBlock extends BlockWithEntity {
    public JackBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new JackBlockEntity(pos, state);
    }
}
