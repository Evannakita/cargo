package evannakita.cargo.entity;

import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

import evannakita.cargo.block.AbstractTrackBlock;
import evannakita.cargo.block.enums.TrackShape;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class BogieEntity extends Entity {
    private static final TrackedData<Integer> DAMAGE_WOBBLE_TICKS = DataTracker.registerData(BogieEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> DAMAGE_WOBBLE_SIDE = DataTracker.registerData(BogieEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> DAMAGE_WOBBLE_STRENGTH = DataTracker.registerData(BogieEntity.class, TrackedDataHandlerRegistry.FLOAT);

    private static final Map<TrackShape, Pair<Vec3i, Vec3i>> ADJACENT_TRACK_POSITIONS_BY_SHAPE = Util.make(Maps.newEnumMap(TrackShape.class), map -> {
        Vec3i n = Direction.NORTH.getVector();
        Vec3i ne = n.east();
        Vec3i e = Direction.EAST.getVector();
        Vec3i se = e.south();
        Vec3i s = Direction.SOUTH.getVector();
        Vec3i sw = s.west();
        Vec3i w = Direction.WEST.getVector();
        Vec3i nw = w.north();
        map.put(TrackShape.NORTH_SOUTH, Pair.of(n, s));
        map.put(TrackShape.EAST_WEST, Pair.of(e, w));
        map.put(TrackShape.NORTHEAST_SOUTHWEST, Pair.of(ne, sw));
        map.put(TrackShape.NORTHWEST_SOUTHEAST, Pair.of(nw, se));
        map.put(TrackShape.NORTH_SOUTHEAST, Pair.of(n, se));
        map.put(TrackShape.NORTH_SOUTHWEST, Pair.of(n, sw));
        map.put(TrackShape.EAST_SOUTHWEST, Pair.of(e, sw));
        map.put(TrackShape.EAST_NORTHWEST, Pair.of(e, nw));
        map.put(TrackShape.SOUTH_NORTHEAST, Pair.of(s, ne));
        map.put(TrackShape.SOUTH_NORTHWEST, Pair.of(s, nw));
        map.put(TrackShape.WEST_SOUTHEAST, Pair.of(w, se));
        map.put(TrackShape.WEST_NORTHEAST, Pair.of(w, ne));
    });

    public BogieEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
    }

    protected BogieEntity(EntityType<?> type, World world, double x, double y, double z) {
        this(type, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(DAMAGE_WOBBLE_TICKS, 0);
        this.dataTracker.startTracking(DAMAGE_WOBBLE_SIDE, 1);
        this.dataTracker.startTracking(DAMAGE_WOBBLE_STRENGTH, Float.valueOf(0.0f));
    }

    private static Pair<Vec3i, Vec3i> getAdjacentTrackPositionsByShape(TrackShape shape) {
        return ADJACENT_TRACK_POSITIONS_BY_SHAPE.get(shape);
    }

    @Nullable
    public Vec3d snapPositionToTrackWithOffset(double x, double y, double z, double offset) {
        BlockState blockState;
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        if (this.getWorld().getBlockState(new BlockPos(i, j - 1, k)).isIn(BlockTags.RAILS)) {
            --j;
        }
        if (AbstractTrackBlock.isTrack(blockState = this.getWorld().getBlockState(new BlockPos(i, j, k)))) {
            TrackShape trackShape = blockState.get(((AbstractTrackBlock)blockState.getBlock()).getTrackShapeProperty());
            y = j;
            if (trackShape.isAscending()) {
                y = j + 1;
            }
            Pair<Vec3i, Vec3i> pair = BogieEntity.getAdjacentTrackPositionsByShape(trackShape);
            Vec3i vec3i = pair.getFirst();
            Vec3i vec3i2 = pair.getSecond();
            double d = vec3i2.getX() - vec3i.getX();
            double e = vec3i2.getZ() - vec3i.getZ();
            double f = Math.sqrt(d * d + e * e);
            if (vec3i.getY() != 0 && MathHelper.floor(x += (d /= f) * offset) - i == vec3i.getX() && MathHelper.floor(z += (e /= f) * offset) - k == vec3i.getZ()) {
                y += (double)vec3i.getY();
            } else if (vec3i2.getY() != 0 && MathHelper.floor(x) - i == vec3i2.getX() && MathHelper.floor(z) - k == vec3i2.getZ()) {
                y += (double)vec3i2.getY();
            }
            return this.snapPositionToTrack(x, y, z);
        }
        return null;
    }

    @Nullable
    public Vec3d snapPositionToTrack(double x, double y, double z) {
        BlockState blockState;
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        if (this.getWorld().getBlockState(new BlockPos(i, j - 1, k)).isIn(BlockTags.RAILS)) {
            --j;
        }
        if (AbstractTrackBlock.isTrack(blockState = this.getWorld().getBlockState(new BlockPos(i, j, k)))) {
            double p;
            TrackShape trackShape = blockState.get(((AbstractTrackBlock)blockState.getBlock()).getTrackShapeProperty());
            Pair<Vec3i, Vec3i> pair = BogieEntity.getAdjacentTrackPositionsByShape(trackShape);
            Vec3i vec3i = pair.getFirst();
            Vec3i vec3i2 = pair.getSecond();
            double d = (double)i + 0.5 + (double)vec3i.getX() * 0.5;
            double e = (double)j + 0.0625 + (double)vec3i.getY() * 0.5;
            double f = (double)k + 0.5 + (double)vec3i.getZ() * 0.5;
            double g = (double)i + 0.5 + (double)vec3i2.getX() * 0.5;
            double h = (double)j + 0.0625 + (double)vec3i2.getY() * 0.5;
            double l = (double)k + 0.5 + (double)vec3i2.getZ() * 0.5;
            double m = g - d;
            double n = (h - e) * 2.0;
            double o = l - f;
            if (m == 0.0) {
                p = z - (double)k;
            } else if (o == 0.0) {
                p = x - (double)i;
            } else {
                double q = x - d;
                double r = z - f;
                p = (q * m + r * o) * 2.0;
            }
            x = d + m * p;
            y = e + n * p;
            z = f + o * p;
            if (n < 0.0) {
                y += 1.0;
            } else if (n > 0.0) {
                y += 0.5;
            }
            return new Vec3d(x, y, z);
        }
        return null;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound var1) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound var1) {
    }

    public float getDamageWobbleStrength() {
        return this.dataTracker.get(DAMAGE_WOBBLE_STRENGTH).floatValue();
    }

    public int getDamageWobbleTicks() {
        return this.dataTracker.get(DAMAGE_WOBBLE_TICKS);
    }

    public int getDamageWobbleSide() {
        return this.dataTracker.get(DAMAGE_WOBBLE_SIDE);
    }
}