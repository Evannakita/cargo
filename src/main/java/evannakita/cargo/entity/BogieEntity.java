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
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class BogieEntity extends Entity {
    private static final TrackedData<Integer> DAMAGE_WOBBLE_TICKS = DataTracker.registerData(BogieEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> DAMAGE_WOBBLE_SIDE = DataTracker.registerData(BogieEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> DAMAGE_WOBBLE_STRENGTH = DataTracker.registerData(BogieEntity.class, TrackedDataHandlerRegistry.FLOAT);
    
    private boolean yawFlipped;
    private boolean onTrack;

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
        map.put(TrackShape.NORTH_TOP, Pair.of(n, s.down()));
        map.put(TrackShape.NORTH_MIDDLE, Pair.of(n, s.down()));
        map.put(TrackShape.NORTH_BOTTOM, Pair.of(n, s));
        map.put(TrackShape.EAST_TOP, Pair.of(e, w.down()));
        map.put(TrackShape.EAST_MIDDLE, Pair.of(e, w.down()));
        map.put(TrackShape.EAST_BOTTOM, Pair.of(e, w));
        map.put(TrackShape.SOUTH_TOP, Pair.of(s, n.down()));
        map.put(TrackShape.SOUTH_MIDDLE, Pair.of(s, n.down()));
        map.put(TrackShape.SOUTH_BOTTOM, Pair.of(s, n));
        map.put(TrackShape.WEST_TOP, Pair.of(w, e.down()));
        map.put(TrackShape.WEST_MIDDLE, Pair.of(w, e.down()));
        map.put(TrackShape.WEST_BOTTOM, Pair.of(w, e));
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

    @Override
    public boolean collidesWith(Entity other) {
        return BoatEntity.canCollide(this, other);
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        World world = this.getWorld();
        if (world.isClient || this.isRemoved()) {
            return true;
        }
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        this.setDamageWobbleSide(-this.getDamageWobbleSide());
        this.setDamageWobbleTicks(10);
        this.scheduleVelocityUpdate();
        this.setDamageWobbleStrength(this.getDamageWobbleStrength() + amount * 10.0f);
        this.emitGameEvent(GameEvent.ENTITY_DAMAGE, source.getAttacker());
        boolean bl = source.getAttacker() instanceof PlayerEntity && ((PlayerEntity)source.getAttacker()).getAbilities().creativeMode;
        if (bl || this.getDamageWobbleStrength() > 40.0f) {
            this.removeAllPassengers();
            if (!bl || this.hasCustomName()) {
                this.dropItems(source);
            } else {
                this.discard();
            }
        }
        return true;
    }

    public void dropItems(DamageSource damageSource) {
        this.kill();
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }

    private static Pair<Vec3i, Vec3i> getAdjacentTrackPositionsByShape(TrackShape shape) {
        return ADJACENT_TRACK_POSITIONS_BY_SHAPE.get(shape);
    }

    @Override
    public void tick() {
        if (this.getDamageWobbleTicks() > 0) {
            this.setDamageWobbleTicks(this.getDamageWobbleTicks() - 1);
        }
        if (this.getDamageWobbleStrength() > 0.0f) {
            this.setDamageWobbleStrength(this.getDamageWobbleStrength() - 1.0f);
        }
        this.attemptTickInVoid();
        this.tickPortal();
        if (!this.hasNoGravity()) {
            double d = this.isTouchingWater() ? -0.005 : -0.04;
            this.setVelocity(this.getVelocity().add(0.0, d, 0.0));
        }
        int i = MathHelper.floor(this.getX());
        int j = MathHelper.floor(this.getY());
        int k = MathHelper.floor(this.getZ());
        if (AbstractTrackBlock.isTrack(this.getWorld(), new BlockPos(i, j - 1, k))) {
            --j;
        }
        BlockPos blockPos = new BlockPos(i, j, k);
        BlockState blockState = this.getWorld().getBlockState(blockPos);
        this.onTrack = AbstractTrackBlock.isTrack(blockState);
        if (this.onTrack) {
            this.moveOnTrack(blockPos, blockState);
        } else {
            this.moveOffTrack();
        }
        this.checkBlockCollision();
        this.setPitch(0.0f);
        double h = this.prevX - this.getX();
        double l = this.prevZ - this.getZ();
        if (h * h + l * l > 0.001) {
            this.setYaw((float)(MathHelper.atan2(l, h) * 180.0 / Math.PI));
            if (this.yawFlipped) {
                this.setYaw(this.getYaw() + 180.0f);
            }
        }
        double m = (double)MathHelper.wrapDegrees(this.getYaw() - this.prevYaw);
        if (m < -170.0 || m >= 170.0) {
            this.setYaw(this.getYaw() + 180.0f);
            this.yawFlipped = !this.yawFlipped;
        }
        this.setRotation(this.getYaw(), this.getPitch());
        this.updateWaterState();
        if (this.isInLava()) {
            this.setOnFireFromLava();
            this.fallDistance *= 0.5f;
        }
        this.firstUpdate = false;
    }

    protected double getMaxSpeed() {
        return (this.isTouchingWater() ? 4.0 : 8.0) / 20.0;
    }

    protected void moveOffTrack() {
        double d = this.getMaxSpeed();
        Vec3d vec3d = this.getVelocity();
        this.setVelocity(MathHelper.clamp(vec3d.x, -d, d), vec3d.y, MathHelper.clamp(vec3d.z, -d, d));
        if (this.isOnGround()) {
            this.setVelocity(this.getVelocity().multiply(0.5));
        }
        this.move(MovementType.SELF, this.getVelocity());
        if (!this.isOnGround()) {
            this.setVelocity(this.getVelocity().multiply(0.95));
        }
    }

    protected void moveOnTrack(BlockPos pos, BlockState state) {
        this.onLanding();
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        // Vec3d snappedPos = this.snapPositionToTrack(d, e, f);
        Vec3d velocity = this.getVelocity();
        e = pos.getY();
        TrackShape trackShape = state.get(((AbstractTrackBlock)state.getBlock()).getTrackShapeProperty());
        Pair<Vec3i, Vec3i> pair = BogieEntity.getAdjacentTrackPositionsByShape(trackShape);
        Vec3i first = pair.getFirst();
        Vec3i second = pair.getSecond();
        // x and z distances between first and second
        double h = second.getX() - first.getX();
        double i = second.getZ() - first.getZ();
        // horizontal distance from first to second
        double j = Math.sqrt(h * h + i * i);
        if ((velocity.x * h + velocity.z * i) < 0.0) {
            h = -h;
            i = -i;
        }
        double l = Math.min(2.0, velocity.horizontalLength());
        // setting direction of velocity to match endpoints
        velocity = new Vec3d(l * h / j, velocity.y, l * i / j);
        this.setVelocity(velocity);
        // double o = (double)pos.getX() + 0.5 + (double)first.getX() * 0.5;
        // double p = (double)pos.getZ() + 0.5 + (double)first.getZ() * 0.5;
        // double q = (double)pos.getX() + 0.5 + (double)second.getX() * 0.5;
        // double r = (double)pos.getZ() + 0.5 + (double)second.getZ() * 0.5;
        // double s;
        double t;
        double u;
        // h = q - o;
        // i = r - p;
        // if (h == 0.0) {
        //     s = f - (double)pos.getZ();
        // } else if (i == 0.0) {
        //     s = d - (double)pos.getX();
        // } else {
        //     t = d - o;
        //     u = f - p;
        //     s = (t * h + u * i) * 2.0;
        // }
        // d = o + h * s;
        // f = p + i * s;
        // this.setPosition(d, e, f);
        t = this.hasPassengers() ? 0.75 : 1.0;
        u = this.getMaxSpeed();
        // velocity = this.getVelocity();
        this.move(MovementType.SELF, new Vec3d(MathHelper.clamp(t * velocity.x, -u, u), 0.0, MathHelper.clamp(t * velocity.z, -u, u)));
        // if (first.getY() != 0 && MathHelper.floor(this.getX()) - pos.getX() == first.getX() && MathHelper.floor(this.getZ()) - pos.getZ() == first.getZ()) {
        //     this.setPosition(this.getX(), this.getY() + (double)first.getY(), this.getZ());
        // } else if (second.getY() != 0 && MathHelper.floor(this.getX()) - pos.getX() == second.getX() && MathHelper.floor(this.getZ()) - pos.getZ() == second.getZ()) {
        //     this.setPosition(this.getX(), this.getY() + (double)second.getY(), this.getZ());
        // }
        this.applySlowdown();
        // Vec3d vec3d4 = this.snapPositionToTrack(this.getX(), this.getY(), this.getZ());
        // Vec3d vec3d5;
        // double w;
        // if (vec3d4 != null && snappedPos != null) {
        //     double v = (snappedPos.y - vec3d4.y) * 0.05;
        //     vec3d5 = this.getVelocity();
        //     w = vec3d5.horizontalLength();
        //     if (w > 0.0) {
        //         this.setVelocity(vec3d5.multiply((w + v) / w, 1.0, (w + v) / w));
        //     }
        //     this.setPosition(this.getX(), vec3d4.y, this.getZ());
        // }
        // int x = MathHelper.floor(this.getX());
        // int y = MathHelper.floor(this.getZ());
        // if (x != pos.getX() || y != pos.getZ()) {
        //     vec3d5 = this.getVelocity();
        //     w = vec3d5.horizontalLength();
        //     this.setVelocity(w * (double)(x - pos.getX()), vec3d5.y, w * (double)(y - pos.getZ()));
        // }
    }

    protected void applySlowdown() {
        double d = this.hasPassengers() ? 0.997 : 0.96;
        Vec3d vec3d = this.getVelocity();
        vec3d = vec3d.multiply(d, 0.0, d);
        if (this.isTouchingWater()) {
            vec3d = vec3d.multiply(0.95f);
        }
        this.setVelocity(vec3d);
    }

    @Nullable
    public Vec3d snapPositionToTrackWithOffset(double x, double y, double z, double offset) {
        BlockState blockState;
        // coordinates of current block
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        if (AbstractTrackBlock.isTrack(this.getWorld(), new BlockPos(i, j - 1, k))) {
            --j;
        }
        if (AbstractTrackBlock.isTrack(blockState = this.getWorld().getBlockState(new BlockPos(i, j, k)))) {
            TrackShape trackShape = blockState.get(((AbstractTrackBlock)blockState.getBlock()).getTrackShapeProperty());
            y = j;
            if (trackShape.isAscending()) {
                y = j + 1;
            }
            Pair<Vec3i, Vec3i> pair = BogieEntity.getAdjacentTrackPositionsByShape(trackShape);
            Vec3i first = pair.getFirst();
            Vec3i second = pair.getSecond();
            // x and z distances between first and second
            double d = second.getX() - first.getX();
            double e = second.getZ() - first.getZ();
            // horizontal distance from first to second
            double f = Math.sqrt(d * d + e * e);
            if (first.getY() != 0 && MathHelper.floor(x += (d /= f) * offset) - i == first.getX() && MathHelper.floor(z += (e /= f) * offset) - k == first.getZ()) {
                y += (double)first.getY();
            } else if (second.getY() != 0 && MathHelper.floor(x) - i == second.getX() && MathHelper.floor(z) - k == second.getZ()) {
                y += (double)second.getY();
            }
            return this.snapPositionToTrack(x, y, z);
        }
        return null;
    }

    @Nullable
    public Vec3d snapPositionToTrack(double x, double y, double z) {
        BlockState blockState;
        // coordinates of current block
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        if (AbstractTrackBlock.isTrack(this.getWorld(), new BlockPos(i, j - 1, k))) {
            --j;
        }
        if (AbstractTrackBlock.isTrack(blockState = this.getWorld().getBlockState(new BlockPos(i, j, k)))) {
            TrackShape trackShape = blockState.get(((AbstractTrackBlock)blockState.getBlock()).getTrackShapeProperty());
            Pair<Vec3i, Vec3i> pair = BogieEntity.getAdjacentTrackPositionsByShape(trackShape);
            Vec3i first = pair.getFirst();
            Vec3i second = pair.getSecond();
            // (d, e, f) is coords halfway to first
            double d = (double)i + 0.5 + (double)first.getX() * 0.5;
            double e = (double)j + 0.0625 + (double)first.getY() * 0.5;
            double f = (double)k + 0.5 + (double)first.getZ() * 0.5;
            // (g, h, i) is coords halfway to second
            double g = (double)i + 0.5 + (double)second.getX() * 0.5;
            double h = (double)j + 0.0625 + (double)second.getY() * 0.5;
            double l = (double)k + 0.5 + (double)second.getZ() * 0.5;
            // n is vertical distance between first and second
            double n = (h - e) * 1.0;
            // p is fraction of distance from first to second the entity should be
            double p;
            if (g - d == 0.0) {
                // if track is straight along z, p is z coordinate in block
                p = z - (double)k;
            } else if (l - f == 0.0) {
                // if track is straight along x, p is x coordinate in block
                p = x - (double)i;
            } else if (g - d == l - f) {
                p = 0.5; //((x - d) * (g - d) + (z - f) * (l - f)) * 2.0;
            } else {
                p = 0.5; // ((x - d) * (g - d) + (z - f) * (l - f)) * 2.0;
            }
            x = d + (g - d) * p;
            y = e + n * p;
            z = f + (l - f) * p;
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

    public void setDamageWobbleStrength(float damageWobbleStrength) {
        this.dataTracker.set(DAMAGE_WOBBLE_STRENGTH, Float.valueOf(damageWobbleStrength));
    }

    public float getDamageWobbleStrength() {
        return this.dataTracker.get(DAMAGE_WOBBLE_STRENGTH).floatValue();
    }

    public void setDamageWobbleTicks(int wobbleTicks) {
        this.dataTracker.set(DAMAGE_WOBBLE_TICKS, wobbleTicks);
    }

    public int getDamageWobbleTicks() {
        return this.dataTracker.get(DAMAGE_WOBBLE_TICKS);
    }

    public void setDamageWobbleSide(int wobbleSide) {
        this.dataTracker.set(DAMAGE_WOBBLE_SIDE, wobbleSide);
    }

    public int getDamageWobbleSide() {
        return this.dataTracker.get(DAMAGE_WOBBLE_SIDE);
    }
}