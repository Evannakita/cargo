package evannakita.cargo.entity;

import evannakita.cargo.block.AbstractTrackBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FlatbedEntity extends Entity {
    private EntityDimensions realDimensions;
    private boolean yawFlipped;
    private BlockPattern blockPattern;

    public FlatbedEntity(EntityType<? extends Entity> entityType, World world) {
        super(entityType, world);
    }

    // public FlatbedEntity(World world, double x, double y, double z, BlockPattern blockPattern) {
    //     this((EntityType<FlatbedEntity>)Cargo.FLATBED, world);
    //     this.setPosition(x, y, z);
    //     this.prevX = x;
    //     this.prevY = y;
    //     this.prevZ = z;
    //     this.blockPattern = blockPattern;
    //     this.realDimensions = new EntityDimensions(Integer.min(blockPattern.getWidth(), blockPattern.getDepth()), blockPattern.getHeight(), false);
    //     this.setBoundingBox(new Box(x, y, z, x + blockPattern.getDepth(), y+blockPattern.getHeight(), z+blockPattern.getWidth()));
    //     this.intersectionChecked = true;
    // }

    // public static FlatbedEntity create(World world, double x, double y, double z, BlockPattern blockPattern) {
    //     if (blockPattern != null) {
    //         return new FlatbedEntity(world, x, y, z, blockPattern);
    //     }
    //     return null;
    // }

    public void setDimensions(EntityDimensions dimensions) {
        this.realDimensions = dimensions;
    }

    public void setBlockPattern(BlockPattern blockPattern) {
        this.blockPattern = blockPattern;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return this.realDimensions;
    }

    public BlockPattern getBlockPattern() {
        return this.blockPattern;
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.EVENTS;
    }

    @Override
    protected void initDataTracker() {
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
    protected float getVelocityMultiplier() {
        BlockState blockState = this.world.getBlockState(this.getBlockPos());
        if (blockState.getBlock() instanceof AbstractTrackBlock) {
            return 1.0f;
        }
        return super.getVelocityMultiplier();
    }

    @Override
    public Direction getMovementDirection() {
        return this.yawFlipped ? this.getHorizontalFacing().getOpposite().rotateYClockwise() : this.getHorizontalFacing().rotateYClockwise();
    }

    protected double getMaxSpeed() {
        return (this.isTouchingWater() ? 4.0 : 8.0) / 20.0;
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

    @Override
    protected void readCustomDataFromNbt(NbtCompound var1) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound var1) {
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
}
