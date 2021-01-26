package game;

import config.Config;
import game.mapElements.Projectile;
import game.mapElements.Tank;
import game.worldMap.WorldMap;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Game implements IGameEventPublisher {
    private final Config config;
    private final WorldMap map;
    private final List<IGameObserver> observers = new LinkedList<>();
    private final List<Tank> enemyTanks = new LinkedList<>();
    private final List<Projectile> projectiles = new LinkedList<>();
    private Tank playerTank;


    public Game(Config config) {
        this.config = config;
        this.map = new WorldMap(config);
    }

    public void start() {
        playerTank = new Tank(map, observers, true);
    }

    public void rotatePlayerTank(int direction) {
        playerTank.rotate(direction);
    }

    public void movePlayerTank(boolean isForward) {
        if(!playerTank.move(isForward)) {
            return;
        };
        endTurn();
    }

    public void shoot() {
        projectiles.add(new Projectile(playerTank.getOrientation(), playerTank.getPosition(), map, observers));
        endTurn();
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

    private void endTurn() {
        for (Projectile projectile: projectiles) {
            projectile.move();
        }
        for (IGameObserver observer: observers) {
            observer.handleTurnEnd();
        }
    }
}
