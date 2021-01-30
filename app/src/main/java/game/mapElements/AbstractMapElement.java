package game.mapElements;

import game.IGameEventPublisher;
import game.IGameObserver;
import game.worldMap.WorldMap;
import utils.Vector2d;
import java.util.List;
import java.util.function.Consumer;

abstract public class AbstractMapElement implements IMapElement {
    protected Vector2d position;
    private final List<IGameObserver> observers;
    protected final WorldMap map;

    public AbstractMapElement(Vector2d position, WorldMap map, List<IGameObserver> observers) {
        this.observers = observers;
        this.position = position;
        this.map = map;
    }

    @Override
    abstract public boolean isBlocking();

    @Override
    abstract public void getShoot();

    @Override
    public Vector2d getPosition() {
        return position;
    }

    protected void notifyObservers(Consumer<IGameObserver> action) {
        for (IGameObserver observer: observers) {
            action.accept(observer);
        }
    }
}
