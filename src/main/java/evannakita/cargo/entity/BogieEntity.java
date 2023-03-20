package evannakita.cargo.entity;

import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

import evannakita.cargo.Cargo;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class BogieEntity extends Entity {
    private static final TrackedData<Integer> DAMAGE_WOBBLE_TICKS = DataTracker.registerData(BogieEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> DAMAGE_WOBBLE_SIDE = DataTracker.registerData(BogieEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> DAMAGE_WOBBLE_STRENGTH = DataTracker.registerData(BogieEntity.class, TrackedDataHandlerRegistry.FLOAT);
    protected static final float VELOCITY_SLOWDOWN_MULTIPLIER = 0.95f;
    private boolean yawFlipped;
    private int clientInterpolationSteps;
    private double clientX;
    private double clientY;
    private double clientZ;
    private double clientYaw;
    private double clientPitch;
    private double clientXVelocity;
    private double clientYVelocity;
    private double clientZVelocity;

    private static final Map<TrackShape, Pair<Vec3i, Vec3i>> ADJACENT_TRACK_POSITIONS_BY_SHAPE = Util.make(Maps.newEnumMap(TrackShape.class), map -> {
        Vec3i vecN = Direction.NORTH.getVector();
        Vec3i vecE = Direction.EAST.getVector();
        Vec3i vecS = Direction.SOUTH.getVector();
        Vec3i vecW = Direction.WEST.getVector();
        Vec3i vecNE = vecN.east();
        Vec3i vecSE = vecS.east();
        Vec3i vecSW = vecS.west();
        Vec3i vecNW = vecN.west();
        map.put(TrackShape.NORTH_SOUTH, Pair.of(vecN, vecS));
        map.put(TrackShape.EAST_WEST, Pair.of(vecE, vecW));
        map.put(TrackShape.NORTH_SOUTHEAST, Pair.of(vecN, vecSE));
        map.put(TrackShape.NORTH_SOUTHWEST, Pair.of(vecN, vecSW));
        map.put(TrackShape.EAST_SOUTHWEST, Pair.of(vecE, vecSW));
        map.put(TrackShape.EAST_NORTHWEST, Pair.of(vecE, vecNW));
        map.put(TrackShape.SOUTH_NORTHWEST, Pair.of(vecS, vecNW));
        map.put(TrackShape.SOUTH_NORTHEAST, Pair.of(vecS, vecNE));
        map.put(TrackShape.WEST_NORTHEAST, Pair.of(vecW, vecNE));
        map.put(TrackShape.WEST_SOUTHEAST, Pair.of(vecW, vecSE));
    });

    public BogieEntity(EntityType<? extends Entity> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
    }

    public BogieEntity(World world, double x, double y, double z) {
        this((EntityType<? extends Entity>)Cargo.BOGIE, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public static BogieEntity create(World world, double x, double y, double z) {
        return new BogieEntity(world, x, y, z);
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.EVENTS;
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
        boolean bl;
        if (this.world.isClient || this.isRemoved()) {
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
        bl = source.getAttacker() instanceof PlayerEntity && ((PlayerEntity)source.getAttacker()).getAbilities().creativeMode;
        if (bl || this.getDamageWobbleStrength() > 40.0f) {
            this.removeAllPassengers();
            if (!bl || this.hasCustomName()) {
                this.kill();
                if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                    this.dropStack(new ItemStack(Cargo.TRAIN_WHEELS, 2));
                }        
            } else {
                this.discard();
            }
        }
        return true;
    }

    @Override
    protected float getVelocityMultiplier() {
        BlockState blockState = this.world.getBlockState(this.getBlockPos());
        if (blockState.getBlock() instanceof AbstractTrackBlock) {
            return 1.0f;
        }
        return super.getVelocityMultiplier();
    }

    @Override
    public void animateDamage() {
        this.setDamageWobbleSide(-this.getDamageWobbleSide());
        this.setDamageWobbleTicks(10);
        this.setDamageWobbleStrength(this.getDamageWobbleStrength() + this.getDamageWobbleStrength() * 10.0f);
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }

    private static Pair<Vec3i, Vec3i> getAdjacentTrackPositionsByShape(TrackShape shape) {
        return ADJACENT_TRACK_POSITIONS_BY_SHAPE.get(shape);
    }

    @Override
    public Direction getMovementDirection() {
        return this.yawFlipped ? this.getHorizontalFacing().getOpposite().rotateYClockwise() : this.getHorizontalFacing().rotateYClockwise();
    }

    @Override
    public void tick() {
        double m;
        BlockPos blockPos;
        if (this.getDamageWobbleTicks() > 0) {
            this.setDamageWobbleTicks(this.getDamageWobbleTicks() - 1);
        }
        if (this.getDamageWobbleStrength() > 0.0f) {
            this.setDamageWobbleStrength(this.getDamageWobbleStrength() - 1.0f);
        }
        this.attemptTickInVoid();
        this.tickPortal();
        if (this.world.isClient) {
            if (this.clientInterpolationSteps > 0) {
                double d = this.getX() + (this.clientX - this.getX()) / (double)this.clientInterpolationSteps;
                double e = this.getY() + (this.clientY - this.getY()) / (double)this.clientInterpolationSteps;
                double f = this.getZ() + (this.clientZ - this.getZ()) / (double)this.clientInterpolationSteps;
                double g = MathHelper.wrapDegrees(this.clientYaw - (double)this.getYaw());
                this.setYaw(this.getYaw() + (float)g / (float)this.clientInterpolationSteps);
                this.setPitch(this.getPitch() + (float)(this.clientPitch - (double)this.getPitch()) / (float)this.clientInterpolationSteps);
                --this.clientInterpolationSteps;
                this.setPosition(d, e, f);
                this.setRotation(this.getYaw(), this.getPitch());
            } else {
                this.refreshPosition();
                this.setRotation(this.getYaw(), this.getPitch());
            }
            return;
        }
        if (!this.hasNoGravity()) {
            double d = this.isTouchingWater() ? -0.005 : -0.04;
            this.setVelocity(this.getVelocity().add(0.0, d, 0.0));
        }
        int k = MathHelper.floor(this.getZ());
        int j = MathHelper.floor(this.getY());
        int i = MathHelper.floor(this.getX());
        BlockState blockState = this.world.getBlockState(blockPos = new BlockPos(i, j - 1, k));
        if (blockState.getBlock() instanceof AbstractTrackBlock) {
            --j;
        } else {
            blockState = this.world.getBlockState(blockPos = new BlockPos(i, j, k));
        }
        if (AbstractTrackBlock.isTrack(blockState)) {
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
        if ((m = (double)MathHelper.wrapDegrees(this.getYaw() - this.prevYaw)) < -170.0 || m >= 170.0) {
            this.setYaw(this.getYaw() + 180.0f);
            this.yawFlipped = !this.yawFlipped;
        }
        this.setRotation(this.getYaw(), this.getPitch());
        for (Entity entity2 : this.world.getOtherEntities(this, this.getBoundingBox().expand(0.2f, 0.0, 0.2f))) {
            if (!entity2.isPushable() || !(entity2 instanceof BogieEntity)) continue;
            entity2.pushAwayFrom(this);
        }
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
        if (this.onGround) {
            this.setVelocity(this.getVelocity().multiply(0.5));
        }
        this.move(MovementType.SELF, this.getVelocity());
        if (!this.onGround) {
            this.setVelocity(this.getVelocity().multiply(0.95));
        }
    }

    protected void moveOnTrack(BlockPos pos, BlockState state) {
        double w;
        Vec3d vec3d5;
        double u;
        double t;
        double s;
        this.onLanding();
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        Vec3d vec3d = this.snapPositionToTrack(d, e, f);
        e = pos.getY();
        Vec3d vec3d2 = this.getVelocity();
        TrackShape trackShape = state.get(((AbstractTrackBlock)state.getBlock()).getTrackShapeProperty());
        Pair<Vec3i, Vec3i> pair = BogieEntity.getAdjacentTrackPositionsByShape(trackShape);
        if (pair == null) return;
        Vec3i vec3i = pair.getFirst();
        Vec3i vec3i2 = pair.getSecond();
        double h = vec3i2.getX() - vec3i.getX();
        double i = vec3i2.getZ() - vec3i.getZ();
        double j = Math.sqrt(h * h + i * i);
        double k = vec3d2.x * h + vec3d2.z * i;
        if (k < 0.0) {
            h = -h;
            i = -i;
        }
        double l = Math.min(2.0, vec3d2.horizontalLength());
        vec3d2 = new Vec3d(l * h / j, vec3d2.y, l * i / j);
        this.setVelocity(vec3d2);
        Entity entity = this.getFirstPassenger();
        if (entity instanceof PlayerEntity) {
            Vec3d vec3d3 = entity.getVelocity();
            double m = vec3d3.horizontalLengthSquared();
            double n = this.getVelocity().horizontalLengthSquared();
            if (m > 1.0E-4 && n < 0.01) {
                this.setVelocity(this.getVelocity().add(vec3d3.x * 0.1, 0.0, vec3d3.z * 0.1));
            }
        }
        double o = (double)pos.getX() + 0.5 + (double)vec3i.getX() * 0.5;
        double p = (double)pos.getZ() + 0.5 + (double)vec3i.getZ() * 0.5;
        double q = (double)pos.getX() + 0.5 + (double)vec3i2.getX() * 0.5;
        double r = (double)pos.getZ() + 0.5 + (double)vec3i2.getZ() * 0.5;
        h = q - o;
        i = r - p;
        if (h == 0.0) {
            s = f - (double)pos.getZ();
        } else if (i == 0.0) {
            s = d - (double)pos.getX();
        } else {
            t = d - o;
            u = f - p;
            s = (t * h + u * i) * 2.0;
        }
        d = o + h * s;
        f = p + i * s;
        this.setPosition(d, e, f);
        t = this.hasPassengers() ? 0.75 : 1.0;
        u = this.getMaxSpeed();
        vec3d2 = this.getVelocity();
        this.move(MovementType.SELF, new Vec3d(MathHelper.clamp(t * vec3d2.x, -u, u), 0.0, MathHelper.clamp(t * vec3d2.z, -u, u)));
        if (vec3i.getY() != 0 && MathHelper.floor(this.getX()) - pos.getX() == vec3i.getX() && MathHelper.floor(this.getZ()) - pos.getZ() == vec3i.getZ()) {
            this.setPosition(this.getX(), this.getY() + (double)vec3i.getY(), this.getZ());
        } else if (vec3i2.getY() != 0 && MathHelper.floor(this.getX()) - pos.getX() == vec3i2.getX() && MathHelper.floor(this.getZ()) - pos.getZ() == vec3i2.getZ()) {
            this.setPosition(this.getX(), this.getY() + (double)vec3i2.getY(), this.getZ());
        }
        this.applySlowdown();
        Vec3d vec3d4 = this.snapPositionToTrack(this.getX(), this.getY(), this.getZ());
        if (vec3d4 != null && vec3d != null) {
            double v = (vec3d.y - vec3d4.y) * 0.05;
            vec3d5 = this.getVelocity();
            w = vec3d5.horizontalLength();
            if (w > 0.0) {
                this.setVelocity(vec3d5.multiply((w + v) / w, 1.0, (w + v) / w));
            }
            this.setPosition(this.getX(), vec3d4.y, this.getZ());
        }
        int x = MathHelper.floor(this.getX());
        int y = MathHelper.floor(this.getZ());
        if (x != pos.getX() || y != pos.getZ()) {
            vec3d5 = this.getVelocity();
            w = vec3d5.horizontalLength();
            this.setVelocity(w * (double)(x - pos.getX()), vec3d5.y, w * (double)(y - pos.getZ()));
        }
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
        int k = MathHelper.floor(z);
        int j = MathHelper.floor(y);
        int i = MathHelper.floor(x);
        BlockState blockState = this.world.getBlockState(new BlockPos(i, j - 1, k));
        if (blockState.getBlock() instanceof AbstractTrackBlock) {
            --j;
        } else {
            blockState = this.world.getBlockState(new BlockPos(i, j, k));
        }
        if (AbstractTrackBlock.isTrack(blockState)) {
            TrackShape trackShape = blockState.get(((AbstractTrackBlock)blockState.getBlock()).getTrackShapeProperty());
            y = j;
            if (trackShape.isAscending()) {
                y = j + 1;
            }
            Pair<Vec3i, Vec3i> pair = BogieEntity.getAdjacentTrackPositionsByShape(trackShape);
            if (pair == null) return null;
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
        int k = MathHelper.floor(z);
        int j = MathHelper.floor(y);
        int i = MathHelper.floor(x);
        BlockState blockState = this.world.getBlockState(new BlockPos(i, j - 1, k));
        if (blockState.getBlock() instanceof AbstractTrackBlock) {
            --j;
        } else {
            blockState = this.world.getBlockState(new BlockPos(i, j, k));
        }
        if (AbstractTrackBlock.isTrack(blockState)) {
            double p;
            TrackShape trackShape = blockState.get(((AbstractTrackBlock)blockState.getBlock()).getTrackShapeProperty());
            Pair<Vec3i, Vec3i> pair = BogieEntity.getAdjacentTrackPositionsByShape(trackShape);
            if (pair == null) return null;
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
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        double e;
        if (this.world.isClient) {
            return;
        }
        if (entity.noClip || this.noClip) {
            return;
        }
        double d = entity.getX() - this.getX();
        double f = d * d + (e = entity.getZ() - this.getZ()) * e;
        if (f >= (double)1.0E-4f) {
            f = Math.sqrt(f);
            d /= f;
            e /= f;
            double g = 1.0 / f;
            if (g > 1.0) {
                g = 1.0;
            }
            d *= g;
            e *= g;
            d *= (double)0.1f;
            e *= (double)0.1f;
            d *= 0.5;
            e *= 0.5;
            this.addVelocity(-d, 0.0, -e);
            entity.addVelocity(d / 4.0, 0.0, e / 4.0);
        }
    }

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        this.clientX = x;
        this.clientY = y;
        this.clientZ = z;
        this.clientYaw = yaw;
        this.clientPitch = pitch;
        this.clientInterpolationSteps = interpolationSteps + 2;
        this.setVelocity(this.clientXVelocity, this.clientYVelocity, this.clientZVelocity);
    }

    @Override
    public void setVelocityClient(double x, double y, double z) {
        this.clientXVelocity = x;
        this.clientYVelocity = y;
        this.clientZVelocity = z;
        this.setVelocity(this.clientXVelocity, this.clientYVelocity, this.clientZVelocity);
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
