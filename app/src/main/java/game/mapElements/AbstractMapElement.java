package game.mapElements;

import game.IGameEventPublisher;
import game.IGameObserver;
import game.worldMap.WorldMap;
import utils.Vector2d;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

abstract public class AbstractMapElement implements IMapElement, IGameEventPublisher {
    private Vector2d position;
    private final List<IGameObserver> observers;
    protected final WorldMap map;

    public AbstractMapElement(Vector2d position, WorldMap map) {
        this.observers = new LinkedList<>();
        this.position = position;
        this.map = map;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public void addObserver(IGameObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void addAllObservers(Collection<? extends IGameObserver> observers) {
        this.observers.addAll(observers);
    }

    @Override
    public void removeObserver(IGameObserver observer) {
        this.observers.remove(observer);
    }

    protected void setPosition(Vector2d position) {
        Vector2d oldPosition = this.position;
        this.position = position;
        notifyObservers(observer -> observer.handleElementPositionChange(this, oldPosition));
    }

    protected void notifyObservers(Consumer<IGameObserver> action) {
        for (IGameObserver observer: observers) {
            action.accept(observer);
        }
    }
}
