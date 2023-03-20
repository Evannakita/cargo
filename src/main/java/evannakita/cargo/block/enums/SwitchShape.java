package evannakita.cargo.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum SwitchShape implements StringIdentifiable {
    NORTH_LEFT("north_left"),
    NORTH_WYE("north_wye"),
    NORTH_RIGHT("north_right"),
    NORTHEAST_LEFT("northeast_left"),
    NORTHEAST_WYE("northeast_wye"),
    NORTHEAST_RIGHT("northeast_right"),
    EAST_LEFT("east_left"),
    EAST_WYE("east_wye"),
    EAST_RIGHT("east_right"),
    SOUTHEAST_LEFT("southeast_left"),
    SOUTHEAST_WYE("southeast_wye"),
    SOUTHEAST_RIGHT("southeast_right"),
    SOUTH_LEFT("south_left"),
    SOUTH_WYE("south_wye"),
    SOUTH_RIGHT("south_right"),
    SOUTHWEST_LEFT("southwest_left"),
    SOUTHWEST_WYE("southwest_wye"),
    SOUTHWEST_RIGHT("southwest_right"),
    WEST_LEFT("west_left"),
    WEST_WYE("west_wye"),
    WEST_RIGHT("west_right"),
    NORTHWEST_LEFT("northwest_left"),
    NORTHWEST_WYE("northwest_wye"),
    NORTHWEST_RIGHT("northwest_right");

    private final String name;

    private SwitchShape(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }

    public boolean isPointing(TrackDirection direction) {
        switch (direction) {
            case NORTH: return
                this == NORTH_LEFT ||
                this == NORTH_WYE ||
                this == NORTH_RIGHT ||
                this == SOUTHEAST_WYE ||
                this == SOUTHEAST_RIGHT ||
                this == SOUTH_LEFT ||
                this == SOUTH_RIGHT ||
                this == SOUTHWEST_LEFT ||
                this == SOUTHWEST_WYE;
            case NORTHEAST: return
                this == NORTHEAST_LEFT ||
                this == NORTHEAST_WYE ||
                this == NORTHEAST_RIGHT ||
                this == SOUTH_WYE ||
                this == SOUTH_RIGHT ||
                this == SOUTHWEST_LEFT ||
                this == SOUTHWEST_RIGHT ||
                this == WEST_LEFT ||
                this == WEST_WYE;
            case EAST: return
                this == EAST_LEFT ||
                this == EAST_WYE ||
                this == EAST_RIGHT ||
                this == SOUTHWEST_WYE ||
                this == SOUTHWEST_RIGHT ||
                this == WEST_LEFT ||
                this == WEST_RIGHT ||
                this == NORTHWEST_LEFT ||
                this == NORTHWEST_WYE;
            case SOUTHEAST: return
                this == NORTH_LEFT ||
                this == NORTH_WYE ||
                this == SOUTHEAST_LEFT ||
                this == SOUTHEAST_WYE ||
                this == SOUTHEAST_RIGHT ||
                this == WEST_WYE ||
                this == WEST_RIGHT ||
                this == NORTHWEST_LEFT ||
                this == NORTHWEST_RIGHT;
            case SOUTH: return
                this == NORTH_LEFT ||
                this == NORTH_RIGHT ||
                this == NORTHEAST_LEFT ||
                this == NORTHEAST_WYE ||
                this == SOUTH_LEFT ||
                this == SOUTH_WYE ||
                this == SOUTH_RIGHT ||
                this == NORTHWEST_WYE ||
                this == NORTHWEST_RIGHT;
            case SOUTHWEST: return
                this == NORTH_WYE ||
                this == NORTH_RIGHT ||
                this == NORTHEAST_LEFT ||
                this == NORTHEAST_RIGHT ||
                this == EAST_LEFT ||
                this == EAST_WYE ||
                this == SOUTHWEST_LEFT ||
                this == SOUTHWEST_WYE ||
                this == SOUTHWEST_RIGHT;
            case WEST: return
                this == NORTHEAST_WYE ||
                this == NORTHEAST_RIGHT ||
                this == EAST_LEFT ||
                this == EAST_RIGHT ||
                this == SOUTHEAST_LEFT ||
                this == SOUTHEAST_WYE ||
                this == WEST_LEFT ||
                this == WEST_WYE ||
                this == WEST_RIGHT;
            case NORTHWEST: return
                this == EAST_WYE ||
                this == EAST_RIGHT ||
                this == SOUTHEAST_LEFT ||
                this == SOUTHEAST_RIGHT ||
                this == SOUTH_LEFT ||
                this == SOUTH_WYE ||
                this == NORTHWEST_LEFT ||
                this == NORTHWEST_WYE ||
                this == NORTHWEST_RIGHT;
            default:
                return false;
        }
    }

    public TrackDirection getRoot() {
        switch (this) {
            case NORTH_LEFT, NORTH_WYE, NORTH_RIGHT:
                return TrackDirection.NORTH;
                
            case NORTHEAST_LEFT, NORTHEAST_WYE, NORTHEAST_RIGHT:
                return TrackDirection.NORTHEAST;

            case EAST_LEFT, EAST_WYE, EAST_RIGHT:
                return TrackDirection.EAST;

            case SOUTHEAST_LEFT, SOUTHEAST_WYE, SOUTHEAST_RIGHT:
                return TrackDirection.SOUTHEAST;

            case SOUTH_LEFT, SOUTH_WYE, SOUTH_RIGHT:
                return TrackDirection.SOUTH;
                
            case SOUTHWEST_LEFT, SOUTHWEST_WYE, SOUTHWEST_RIGHT:
                return TrackDirection.SOUTHWEST;
                
            case WEST_LEFT, WEST_WYE, WEST_RIGHT:
                return TrackDirection.WEST;
                
            case NORTHWEST_LEFT, NORTHWEST_WYE, NORTHWEST_RIGHT:
                return TrackDirection.NORTHWEST;
                
            default:
                return null;
        }
    }

    public TrackShape getTrackShape(Boolean right) {
        if (right) {
            switch (this) {
                case NORTH_LEFT, SOUTH_LEFT:
                    return TrackShape.NORTH_SOUTH;
                case NORTHEAST_LEFT, SOUTHWEST_LEFT:
                    return TrackShape.NORTHEAST_SOUTHWEST;
                case EAST_LEFT, WEST_LEFT:
                    return TrackShape.EAST_WEST;
                case NORTHWEST_LEFT, SOUTHEAST_LEFT:
                    return TrackShape.NORTHWEST_SOUTHEAST;
                case SOUTHEAST_WYE, SOUTHEAST_RIGHT:
                    return TrackShape.NORTH_SOUTHEAST;
                case NORTH_WYE, NORTH_RIGHT:
                    return TrackShape.NORTH_SOUTHWEST;
                case SOUTHWEST_WYE, SOUTHWEST_RIGHT:
                    return TrackShape.EAST_SOUTHWEST;
                case EAST_WYE, EAST_RIGHT:
                    return TrackShape.EAST_NORTHWEST;
                case NORTHWEST_WYE, NORTHWEST_RIGHT:
                    return TrackShape.SOUTH_NORTHWEST;
                case SOUTH_WYE, SOUTH_RIGHT:
                    return TrackShape.SOUTH_NORTHEAST;
                case NORTHEAST_WYE, NORTHEAST_RIGHT:
                    return TrackShape.WEST_NORTHEAST;
                case WEST_WYE, WEST_RIGHT:
                    return TrackShape.WEST_SOUTHEAST;
                default:
                    return null;
            }
        } else {
            switch (this) {
                case NORTH_RIGHT, SOUTH_RIGHT:
                    return TrackShape.NORTH_SOUTH;
                case NORTHEAST_RIGHT, SOUTHWEST_RIGHT:
                    return TrackShape.NORTHEAST_SOUTHWEST;
                case EAST_RIGHT, WEST_RIGHT:
                    return TrackShape.EAST_WEST;
                case NORTHWEST_RIGHT, SOUTHEAST_RIGHT:
                    return TrackShape.NORTHWEST_SOUTHEAST;
                case NORTH_LEFT, NORTH_WYE:
                    return TrackShape.NORTH_SOUTHEAST;
                case SOUTHWEST_LEFT, SOUTHWEST_WYE:
                    return TrackShape.NORTH_SOUTHWEST;
                case EAST_LEFT, EAST_WYE:
                    return TrackShape.EAST_SOUTHWEST;
                case NORTHWEST_LEFT, NORTHWEST_WYE:
                    return TrackShape.EAST_NORTHWEST;
                case SOUTH_LEFT, SOUTH_WYE:
                    return TrackShape.SOUTH_NORTHWEST;
                case NORTHEAST_LEFT, NORTHEAST_WYE:
                    return TrackShape.SOUTH_NORTHEAST;
                case WEST_LEFT, WEST_WYE:
                    return TrackShape.WEST_NORTHEAST;
                case SOUTHEAST_LEFT, SOUTHEAST_WYE:
                    return TrackShape.WEST_SOUTHEAST;
                default:
                    return null;
            }
        }
    }

    @Override
    public String asString() {
        return this.name;
    }
}
