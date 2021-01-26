package game;

import config.Config;
import game.mapElements.Tank;
import game.worldMap.WorldMap;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Game implements IGameEventPublisher {
    private final Config config;
    private final WorldMap map;
    private List<IGameObserver> observers;
    private Tank playerTank;
    private List<Tank> enemyTanks;

    public Game(Config config) {
        this.config = config;
        this.map = new WorldMap(config);
        this.observers = new LinkedList<>();
        this.enemyTanks = new LinkedList<>();
    }

    public void start() {
        playerTank = new Tank(map, observers, true);
    }

    public void rotatePlayerTank(int direction) {
        playerTank.rotate(direction);
    }

    public void movePlayerTank(boolean isForward) {
        playerTank.move(isForward);
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
