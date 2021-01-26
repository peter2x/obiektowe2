package game.worldMap;

import config.Config;
import utils.Vector2d;

public class WorldMap {
    private final Config config;

    public WorldMap(Config config) {
        this.config = config;
    }

    public boolean isOccupied(Vector2d position) {
        return false;
    }

    public Vector2d fitToBorders(Vector2d position) {
        return position.fitToRectangle(new Vector2d(0, 0), new Vector2d(config.width() - 1, config.height() - 1));
    }
}
