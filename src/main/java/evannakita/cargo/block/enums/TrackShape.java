package evannakita.cargo.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum TrackShape implements StringIdentifiable {
    NORTH_SOUTH("north_south"),
    EAST_WEST("east_west"),
    NORTHEAST_SOUTHWEST("northeast_southwest"),
    NORTHWEST_SOUTHEAST("northwest_southeast"),
    NORTH_SOUTHEAST("north_southeast"),
    NORTH_SOUTHWEST("north_southwest"),
    EAST_SOUTHWEST("east_southwest"),
    EAST_NORTHWEST("east_northwest"),
    SOUTH_NORTHWEST("south_northwest"),
    SOUTH_NORTHEAST("south_northeast"),
    WEST_NORTHEAST("west_northeast"),
    WEST_SOUTHEAST("west_southeast"),
    NORTH_TOP("north_top"),
    NORTH_MIDDLE("north_middle"),
    NORTH_BOTTOM("north_bottom"),
    EAST_TOP("east_top"),
    EAST_MIDDLE("east_middle"),
    EAST_BOTTOM("east_bottom"),
    SOUTH_TOP("south_top"),
    SOUTH_MIDDLE("south_middle"),
    SOUTH_BOTTOM("south_bottom"),
    WEST_TOP("west_top"),
    WEST_MIDDLE("west_middle"),
    WEST_BOTTOM("west_bottom");

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
        return this == NORTH_TOP ||
            this == NORTH_MIDDLE ||
            this == NORTH_BOTTOM ||
            this == EAST_TOP ||
            this == EAST_MIDDLE ||
            this == EAST_BOTTOM ||
            this == SOUTH_TOP ||
            this == SOUTH_MIDDLE ||
            this == SOUTH_BOTTOM ||
            this == WEST_TOP ||
            this == WEST_MIDDLE ||
            this == WEST_BOTTOM;
    }

    public boolean isPointing(TrackDirection direction) {
        switch (direction) {
            case NORTH: return
                this == NORTH_SOUTH ||
                this == NORTH_SOUTHEAST ||
                this == NORTH_SOUTHWEST ||
                this == NORTH_TOP ||
                this == NORTH_MIDDLE ||
                this == NORTH_BOTTOM ||
                this == SOUTH_TOP ||
                this == SOUTH_MIDDLE ||
                this == SOUTH_BOTTOM;
            case NORTHEAST: return
                this == NORTHEAST_SOUTHWEST ||
                this == SOUTH_NORTHEAST ||
                this == WEST_NORTHEAST;
            case EAST: return
                this == EAST_WEST ||
                this == EAST_SOUTHWEST ||
                this == EAST_NORTHWEST ||
                this == EAST_TOP ||
                this == EAST_MIDDLE ||
                this == EAST_BOTTOM ||
                this == WEST_TOP ||
                this == WEST_MIDDLE ||
                this == WEST_BOTTOM;
            case SOUTHEAST: return
                this == NORTHWEST_SOUTHEAST ||
                this == NORTH_SOUTHEAST ||
                this == WEST_SOUTHEAST;
            case SOUTH: return
                this == NORTH_SOUTH ||
                this == SOUTH_NORTHEAST ||
                this == SOUTH_NORTHWEST ||
                this == NORTH_TOP ||
                this == NORTH_MIDDLE ||
                this == NORTH_BOTTOM ||
                this == SOUTH_TOP ||
                this == SOUTH_MIDDLE ||
                this == SOUTH_BOTTOM;
            case SOUTHWEST: return
                this == NORTHEAST_SOUTHWEST ||
                this == NORTH_SOUTHWEST ||
                this == EAST_SOUTHWEST;
            case WEST: return
                this == EAST_WEST ||
                this == WEST_NORTHEAST ||
                this == WEST_SOUTHEAST ||
                this == EAST_TOP ||
                this == EAST_MIDDLE ||
                this == EAST_BOTTOM ||
                this == WEST_TOP ||
                this == WEST_MIDDLE ||
                this == WEST_BOTTOM;
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
