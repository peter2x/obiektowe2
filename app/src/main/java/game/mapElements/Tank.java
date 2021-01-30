package game.mapElements;

import game.IGameObserver;
import game.worldMap.WorldMap;
import utils.Vector2d;

import java.util.List;

public class Tank extends AbstractMapElement {
    private Orientation orientation;
    private final boolean isPlayer;

    public Tank(Vector2d position, WorldMap map, List<IGameObserver> observers, boolean isPlayer) {
        super(position, map, observers);
        this.orientation = Orientation.values()[(int)(Math.random() * 8)];
        this.isPlayer = isPlayer;
        notifyObservers(observer -> observer.handleElementAdded(this));
    }

    public void rotate(int direction) {
        orientation = Orientation.values()[(orientation.getValue() + direction + 8) % 8];
        notifyObservers(observer -> observer.handleTankRotate(this, direction));
    }

    @Override
    public void getShoot() {
        notifyObservers(o -> o.handleElementDestroyed(this));
    }

    public boolean isBlocking() {
        return true;
    }

    public void makeTurn() {

    }

    public boolean move(boolean isForward) {
        if (getOrientation().getValue() % 2 != 0) {
            return false;
        }
        Vector2d moveVector = isForward ? getOrientation().toUnitVector() : getOrientation().toUnitVector().opposite();
        Vector2d nextPosition = getPosition().add(moveVector);
        nextPosition = map.fitToBorders(nextPosition);
        if (map.isBlocked(nextPosition)) {
            return false;
        }
        setPosition(nextPosition);
        return true;
    }


    public Orientation getOrientation() {
        return orientation;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    private void setPosition(Vector2d position) {
        Vector2d oldPosition = getPosition();
        this.position = position;
        notifyObservers(observer -> observer.handleTankMoved(this, oldPosition));
    }
}
