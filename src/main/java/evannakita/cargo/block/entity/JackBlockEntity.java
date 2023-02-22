package evannakita.cargo.block.entity;

import evannakita.cargo.Cargo;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class JackBlockEntity extends BlockEntity {

    public JackBlockEntity(BlockPos pos, BlockState state) {
        super(Cargo.JACK_ENTITY, pos, state);
    }
}
