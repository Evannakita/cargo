package evannakita.cargo.block;

import org.jetbrains.annotations.Nullable;

import evannakita.cargo.block.entity.ContainerBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ContainerBlock extends HullBlock {
    public static final BooleanProperty DOOR = BooleanProperty.of("door");

    public ContainerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
            this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(OFFSET, false)
                .with(LEVEL, 1)
                .with(POSITION, Position.MIDDLE)
                .with(DOOR, false)
        );
    }

    public Property<Boolean> getDoorProperty() {
        return DOOR;
    }
    
    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ContainerBlockEntity(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity;
        if (itemStack.hasCustomName() && (blockEntity = world.getBlockEntity(pos)) instanceof ContainerBlockEntity) {
            ((ContainerBlockEntity)blockEntity).setCustomName(itemStack.getName());
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(DOOR)) {
            this.openScreen(world, pos, player);
        }
        return ActionResult.success(world.isClient);
    }

    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ContainerBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory)blockEntity);
            PiglinBrain.onGuardedBlockInteracted(player, true);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OFFSET, LEVEL, POSITION, DOOR);
    }
}