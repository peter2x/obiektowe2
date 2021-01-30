package game.mapElements;

import game.IGameObserver;
import game.worldMap.WorldMap;
import utils.Vector2d;

import java.util.List;

public class Obstacle extends AbstractMapElement {
    public Obstacle(Vector2d position, WorldMap map, List<IGameObserver> observers) {
        super(position, map, observers);
        notifyObservers(observer -> observer.handleElementAdded(this));
    }

    @Override
    public boolean isBlocking() {
        return true;
    }

    @Override
    public void getShoot() {
        notifyObservers(o -> o.handleElementDestroyed(this));
    }
}
