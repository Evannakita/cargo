package evannakita.cargo.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public abstract class AbstractLocomotiveEntity extends AbstractTrainEntity {
    protected AbstractLocomotiveEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
    }

    public static AbstractLocomotiveEntity create(World world, double x, double y, double z, LocomotiveType type) {
        if (type == LocomotiveType.STEAM) {
            return null;
        }
        if (type == LocomotiveType.DIESEL) {
            return null;
        }
        if (type == LocomotiveType.REDSTONE) {
            return null;
        }
        return null;
    }

    public static enum LocomotiveType {
        STEAM,
        DIESEL,
        REDSTONE;
    }
}