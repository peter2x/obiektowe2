package game;

import config.Config;
import game.worldMap.WorldMap;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Game implements IGameEventPublisher {
    private final Config config;
    private final WorldMap map;
    private List<IGameObserver> observers;

    public Game(Config config) {
        this.config = config;
        this.map = new WorldMap(config);
        this.observers = new LinkedList<>();
    }

    @Override
    public void addObserver(IGameObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(IGameObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void addAllObservers(Collection<? extends IGameObserver> observers) {
        this.observers.addAll(observers);
    }
}
