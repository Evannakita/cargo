package evannakita.cargo.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum TrackShape implements StringIdentifiable {
    NORTH_SOUTH("north_south"),
    EAST_WEST("east_west"),
    NORTHEAST_SOUTHWEST("northeast_southwest"),
    NORTHWEST_SOUTHEAST("northwest_southeast"),
    ASCENDING_NORTH("ascending_north"),
    ASCENDING_EAST("ascending_east"),
    ASCENDING_SOUTH("ascending_south"),
    ASCENDING_WEST("ascending_west"),
    NORTH_SOUTHEAST("north_southeast"),
    NORTH_SOUTHWEST("north_southwest"),
    EAST_SOUTHWEST("east_southwest"),
    EAST_NORTHWEST("east_northwest"),
    SOUTH_NORTHWEST("south_northwest"),
    SOUTH_NORTHEAST("south_northeast"),
    WEST_NORTHEAST("west_northeast"),
    WEST_SOUTHEAST("west_southeast");

    private final String name;

    private TrackShape(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }

    public boolean isAscending() {
        return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
    }

    public boolean isPointing(TrackDirection direction) {
        switch (direction) {
            case NORTH: return
                this == NORTH_SOUTH ||
                this == ASCENDING_NORTH ||
                this == ASCENDING_SOUTH ||
                this == NORTH_SOUTHEAST ||
                this == NORTH_SOUTHWEST;
            case NORTHEAST: return
                this == NORTHEAST_SOUTHWEST ||
                this == SOUTH_NORTHEAST ||
                this == WEST_NORTHEAST;
            case EAST: return
                this == EAST_WEST ||
                this == ASCENDING_EAST ||
                this == ASCENDING_WEST ||
                this == EAST_SOUTHWEST ||
                this == EAST_NORTHWEST;
            case SOUTHEAST: return
                this == NORTHWEST_SOUTHEAST ||
                this == NORTH_SOUTHEAST ||
                this == WEST_SOUTHEAST;
            case SOUTH: return
                this == NORTH_SOUTH ||
                this == ASCENDING_NORTH ||
                this == ASCENDING_SOUTH ||
                this == SOUTH_NORTHEAST ||
                this == SOUTH_NORTHWEST;
            case SOUTHWEST: return
                this == NORTHEAST_SOUTHWEST ||
                this == NORTH_SOUTHWEST ||
                this == EAST_SOUTHWEST;
            case WEST: return
                this == EAST_WEST ||
                this == ASCENDING_EAST ||
                this == ASCENDING_WEST ||
                this == WEST_NORTHEAST ||
                this == WEST_SOUTHEAST;
            case NORTHWEST: return
                this == NORTHWEST_SOUTHEAST ||
                this == EAST_NORTHWEST ||
                this == SOUTH_NORTHWEST;
            default:
                return false;
        }
    }

    @Override
    public String asString() {
        return this.name;
    }
}
