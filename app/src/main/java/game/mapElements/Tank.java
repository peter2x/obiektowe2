package game.mapElements;

import game.worldMap.WorldMap;
import utils.Vector2d;

public class Tank extends AbstractMapElement {
    private int orientation = 0;

    public Tank(WorldMap map) {
        super(new Vector2d(2, 2), map);
        this.orientation = (int)(Math.random() * 8);
    }

    public void rotate(int direction) {
        orientation += direction;
        orientation = (orientation + 8) % 8;
        notifyObservers(observer -> observer.handleTankRotate(this, direction));
    }

    public int getOrientation() {
        return orientation;
    }
}
