package game.mapElements;

import utils.Vector2d;

public enum Orientation {
    EAST(0),
    SOUTH_EAST(1),
    SOUTH(2),
    SOUTH_WEST(3),
    WEST(4),
    NORTH_WEST(5),
    NORTH(6),
    NORTH_EAST(7);

    private final int value;

    Orientation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Vector2d toUnitVector() {
        return switch (value) {
            case 0 -> new Vector2d(1, 0);
            case 1 -> new Vector2d(1, -1);
            case 2 -> new Vector2d(0, -1);
            case 3 -> new Vector2d(-1, -1);
            case 4 -> new Vector2d(-1, 0);
            case 5 -> new Vector2d(-1, 1);
            case 6 -> new Vector2d(0, 1);
            case 7 -> new Vector2d(1, 1);
            default -> null;
        };
    }
}
