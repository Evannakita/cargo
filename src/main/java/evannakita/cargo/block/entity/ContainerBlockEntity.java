package evannakita.cargo.block.entity;

import evannakita.cargo.Cargo;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class ContainerBlockEntity extends LootableContainerBlockEntity {
    public static final int INVENTORY_SIZE = 9;
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);

    public ContainerBlockEntity(BlockPos pos, BlockState state) {
        super(Cargo.CONTAINER_BLOCK_ENTITY, pos, state);
    }

    @Override
    public int size() {
        return INVENTORY_SIZE;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("cargo.boxcar_hull");
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new Generic3x3ContainerScreenHandler(syncId, playerInventory, this);
    }
}