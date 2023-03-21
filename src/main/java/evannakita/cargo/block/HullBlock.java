package evannakita.cargo.block;

import evannakita.cargo.Cargo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class HullBlock extends HorizontalFacingBlock {
    public static final BooleanProperty OFFSET = BooleanProperty.of("offset");
    public static final IntProperty LEVEL = IntProperty.of("level", 0, 3);
    public static final EnumProperty<Position> POSITION = EnumProperty.of("position", Position.class);

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 8.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    public HullBlock(Settings settings) {
        super(settings);
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
        BlockPos pos = new BlockPos(context.getHitPos());
        BlockState placedOn = context.getWorld().getBlockState(pos);
        Direction facing = Direction.NORTH;
        boolean offset = false;
        int level = 0;
        Position position = Position.MIDDLE;
        if (placedOn.isOf(Cargo.TRAIN_STRUCTURE_BLOCK)) {
            level = placedOn.get(LEVEL);
            offset = true;
            if (placedOn.get(TrainStructureBlock.LEVEL) > 0) {
                facing = context.getSide();
            } else if (context.getWorld().getBlockState(pos.offset(placedOn.get(FACING))).isOf(Cargo.TRAIN_STRUCTURE_BLOCK)) {
                facing = placedOn.get(FACING);
            }
        }
        if (placedOn.getBlock() instanceof HullBlock) {
            if (context.getWorld().getBlockState(pos.offset(placedOn.get(FACING))).isOf(Cargo.TRAIN_STRUCTURE_BLOCK)) {
                facing = placedOn.get(FACING);
                offset = true;
            }
        }
        if (context.getWorld().getBlockState(pos.down(level).offset(facing).offset(facing.rotateYClockwise())).isOf(Cargo.TRACK_WITH_COUPLER)) {
            position = Position.END_LEFT;
        }
        if (context.getWorld().getBlockState(pos.down(level).offset(facing).offset(facing.rotateYCounterclockwise())).isOf(Cargo.TRACK_WITH_COUPLER)) {
            position = Position.END_RIGHT;
        }
        return this.getDefaultState().with(FACING, facing).with(OFFSET, offset).with(LEVEL, level).with(POSITION, position);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
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
