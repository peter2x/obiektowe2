package game.mapElements;

import game.IGameObserver;
import game.worldMap.WorldMap;
import utils.Vector2d;

import java.util.List;

public class Projectile extends AbstractMapElement {
    private Orientation orientation;

    public Projectile(Orientation orientation, Vector2d position, WorldMap map, List<IGameObserver> observers) {
        super(position, map, observers);
        this.orientation = orientation;
    }

    public void move() {
        Vector2d nextPosition = getPosition().add(getOrientation().toUnitVector());
        nextPosition = map.fitToBorders(nextPosition);
        setPosition(nextPosition);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    private void setPosition(Vector2d position) {
        Vector2d oldPosition = getPosition();
        this.position = position;
        notifyObservers(observer -> observer.handleProjectileMoved(this, oldPosition));
    }
}
