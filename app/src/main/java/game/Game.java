package game;

import config.Config;
import game.mapElements.Tank;
import game.worldMap.WorldMap;
import utils.Vector2d;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Game implements IGameEventPublisher {
    private final Config config;
    private final WorldMap map;
    private final List<IGameObserver> observers = new LinkedList<>();
    private Tank playerTank;
    private int score = 0;
    private boolean isFinished = false;


    public Game(Config config) {
        this.config = config;
        this.map = new WorldMap(this, config, observers);
        addObserver(map);
    }

    public void start() {
        playerTank = new Tank(new Vector2d(2, 2), map, observers, true);
        map.setPlayerTank(playerTank);
        endTurn();
    }

    public void rotatePlayerTank(int direction) {
        if (isFinished) {
            return;
        }
        playerTank.rotate(direction);
    }

    public void movePlayerTank(boolean isForward) {
        if (isFinished) {
            return;
        }
        if(!playerTank.move(isForward)) {
            return;
        }
        endTurn();
    }

    public void shoot() {
        if (isFinished) {
            return;
        }
        map.shoot(playerTank.getPosition(), playerTank.getOrientation());
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
        for (IGameObserver observer: observers) {
            observer.handleTurnEnd();
        }
    }

    public void end() {
        isFinished = true;
        for (IGameObserver observer: observers) {
            observer.handleGameEnd(score);
        }
    }

    public void incrementScore() {
        score++;
    }


    private void spawnEnemyTanks() {

    }
}
