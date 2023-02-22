package evannakita.cargo.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum JunctionShape implements StringIdentifiable {
    T("t"),
    X("x"),
    NS_LEFT("ns_left"),
    NS_RIGHT("ns_right"),
    EW_LEFT("ew_left"),
    EW_RIGHT("ew_right");

    private final String name;

    private JunctionShape(String name) {
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
            case NORTH, SOUTH: return
                this == T ||
                this == NS_LEFT ||
                this == NS_RIGHT;
            case NORTHEAST, SOUTHWEST: return
                this == X ||
                this == NS_RIGHT ||
                this == EW_LEFT;
            case EAST, WEST: return
                this == T ||
                this == EW_LEFT ||
                this == EW_RIGHT;
            case SOUTHEAST, NORTHWEST: return
                this == X ||
                this == NS_LEFT ||
                this == EW_RIGHT;
            default:
                return false;
        }
    }

    @Override
    public String asString() {
        return this.name;
    }
}
