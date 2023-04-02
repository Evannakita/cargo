package evannakita.cargo.block;

import org.jetbrains.annotations.Nullable;

import evannakita.cargo.Cargo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class HullBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty OFFSET = BooleanProperty.of("offset");
    public static final IntProperty LEVEL = IntProperty.of("level", 1, 2);
    public static final EnumProperty<Position> POSITION = EnumProperty.of("position", Position.class);

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 8.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 16.0, 16.0);

    public HullBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
            this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(OFFSET, false)
                .with(LEVEL, 1)
                .with(POSITION, Position.MIDDLE)
        );
    }

    public Property<Direction> getFacingProperty() {
        return FACING;
    }
    
    public Property<Boolean> getOffsetProperty() {
        return OFFSET;
    }
    
    public Property<Integer> getLevelProperty() {
        return LEVEL;
    }
    
    public Property<Position> getPositionProperty() {
        return POSITION;
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        Direction side = context.getSide();
        BlockPos targetPos = new BlockPos(context.getHitPos());
        BlockState target = world.getBlockState(targetPos);
        Direction facing = context.getPlayerFacing();
        boolean offset = false;
        int level = 1;
        Position position = Position.MIDDLE;
        if (target.isOf(Cargo.TRAIN_STRUCTURE_BLOCK) && target.get(TrainStructureBlock.LEVEL) > 0) {
            offset = true;
            level = target.get(TrainStructureBlock.LEVEL);
            facing = side.getOpposite();
        } else if (target.getBlock() instanceof HullBlock || (target.isOf(Cargo.TRAIN_STRUCTURE_BLOCK) && target.get(TrainStructureBlock.LEVEL) == 0)) {
            Direction targetFacing = target.get(FACING);
            BlockState newTarget = world.getBlockState(pos.offset(targetFacing));
            if (newTarget.isOf(Cargo.TRAIN_STRUCTURE_BLOCK)) {
                facing = targetFacing;
                level = newTarget.get(TrainStructureBlock.LEVEL);
                offset = true;
            }
        }
        BlockState belowState = world.getBlockState(pos.down());
        if (belowState.isOf(this)) {
            if (belowState.get(HullBlock.LEVEL) == 1) {
                level = 2;
            }
        }
        if (world.getBlockState(pos.down(level).offset(facing).offset(facing.rotateYClockwise())).isOf(Cargo.TRACK_WITH_COUPLER)) {
            position = Position.END_LEFT;
        }
        if (world.getBlockState(pos.down(level).offset(facing).offset(facing.rotateYCounterclockwise())).isOf(Cargo.TRACK_WITH_COUPLER)) {
            position = Position.END_RIGHT;
        }
        return this.getDefaultState().with(FACING, facing).with(OFFSET, offset).with(LEVEL, level).with(POSITION, position);
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof Inventory) {
            ItemScatterer.spawn(world, pos, (Inventory)((Object)blockEntity));
            world.updateComparators(pos, this);
        }
        newState.onStateReplaced(world, pos, newState, moved);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (!state.get(OFFSET)) {
            return VoxelShapes.fullCube();
        }
        switch (state.get(FACING)) {
            default:
                return VoxelShapes.fullCube();
            case NORTH:
                return NORTH_SHAPE;
            case EAST:
                return EAST_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
        }
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OFFSET, LEVEL, POSITION);
    }

    public enum Position implements StringIdentifiable {
        END_LEFT("end_left"),
        END_RIGHT("end_right"),
        MIDDLE("middle");

        private final String name;

        private Position(String name) {
            this.name = name;
        }
        @Override
        public String asString() {
            return this.name;
        }
    }
}
