package evannakita.cargo.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public abstract class AbstractLocomotiveEntity extends Entity {
    protected AbstractLocomotiveEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
    }

    protected AbstractLocomotiveEntity(EntityType<?> type, World world, double x, double y, double z) {
        this(type, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public static AbstractLocomotiveEntity create(World world, double x, double y, double z, Type type) {
        if (type == Type.STEAM) {
            return null;
        }
        if (type == Type.DIESEL) {
            return null;
        }
        if (type == Type.REDSTONE) {
            return null;
        }
        return null;
    }

    public static enum Type {
        STEAM,
        DIESEL,
        REDSTONE;
    }
}