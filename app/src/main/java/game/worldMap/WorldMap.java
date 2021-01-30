package game.worldMap;

import config.Config;
import game.Game;
import game.IGameObserver;
import game.mapElements.*;
import utils.Vector2d;

import java.util.*;

public class WorldMap implements IGameObserver {
    private final Config config;
    private final Map<Vector2d, IMapElement> positionToBlockingElement = new HashMap();
    private final Set<Vector2d> freePositions = new HashSet<>();
    private final Map<Vector2d, List<Projectile>> positionToProjectiles = new HashMap<>();
    private final List<Tank> enemyTanks = new LinkedList<>();
    private final List<IGameObserver> observers;
    private final Game game;
    private Tank playerTank;

    public WorldMap(Game game, Config config, List<IGameObserver> observers) {
        this.config = config;
        this.observers = observers;
        this.game = game;
        for (int i = 0; i < config.width(); i++) {
            for (int j = 0; j < config.height(); j++) {
                freePositions.add(new Vector2d(i, j));
            }
        }
    }

    public boolean isBlocked(Vector2d position) {
        return positionToBlockingElement.containsKey(position);
    }

    public IMapElement blockingObjectAt(Vector2d position) {
        return positionToBlockingElement.get(position);
    }

    public Vector2d fitToBorders(Vector2d position) {
        Vector2d lowerLeft = new Vector2d(0, 0);
        Vector2d upperRight =  new Vector2d(config.width() - 1, config.height() - 1);
        return position.fitToRectangle(lowerLeft, upperRight);
    }


    @Override
    public void handleTankRotate(Tank tank, int oldValue) {

    }

    @Override
    public void handleTankMoved(Tank moved, Vector2d oldPosition) {
        Vector2d newPosition = moved.getPosition();
        freePositions.remove(newPosition);
        freePositions.add(oldPosition);
        positionToBlockingElement.remove(oldPosition);
        positionToBlockingElement.put(newPosition, moved);
        positionToProjectiles.putIfAbsent(newPosition, new LinkedList<>());
        if (positionToProjectiles.get(newPosition).removeIf(
                p -> p.getOrientation().toUnitVector().opposite().equals(moved.getOrientation().toUnitVector())
        )) {
            moved.getShoot();
        }
    }

    @Override
    public void handleGameEnd(int finalScore) {

    }

    @Override
    public void handleElementAdded(IMapElement element) {
        if (element instanceof Tank tank) {
            if (!tank.isPlayer()) {
                enemyTanks.add(tank);
            }
        }
        if (element.isBlocking()) {
            positionToBlockingElement.put(element.getPosition(), element);
        }
        freePositions.remove(element.getPosition());
    }

    @Override
    public void handleProjectileMoved(Projectile moved, Vector2d oldPosition) {
        positionToProjectiles.get(oldPosition).remove(moved);
        if (positionToProjectiles.get(oldPosition).isEmpty() && !oldPosition.equals(playerTank.getPosition())) {
            freePositions.add(oldPosition);
        }
        Vector2d newPosition = moved.getPosition();
        if (blockingObjectAt(newPosition) != null) {
            blockingObjectAt(newPosition).getShoot();
        } else {
            positionToProjectiles.putIfAbsent(moved.getPosition(), new LinkedList<>());
            positionToProjectiles.get(moved.getPosition()).add(moved);
            freePositions.remove(moved.getPosition());
        }
    }

    @Override
    public void handleTurnEnd() {
        moveProjectiles();
        moveEnemyTanks();
        spawnObstacles();
        spawnEnemyTanks();
    }

    @Override
    public void handleElementDestroyed(IMapElement element) {
        if (element instanceof Tank tank) {
            if (tank.isPlayer()) {
                game.end();
            } else {
                game.incrementScore();
            }
            enemyTanks.remove(tank);
        }
        if (element.isBlocking()) {
            positionToBlockingElement.remove(element.getPosition());
            freePositions.add(element.getPosition());
        }
    }

    public void shoot(Vector2d playerPosition, Orientation bulletOrientation) {
        positionToProjectiles.putIfAbsent(playerPosition, new LinkedList<>());
        Projectile created = new Projectile(bulletOrientation, playerPosition, this, observers);
        positionToProjectiles.get(playerPosition).add(created);
    }

    private void moveProjectiles() {
        List<Projectile> projectiles = new LinkedList<>();
        for (List<Projectile> projectilesList: positionToProjectiles.values()) {
            projectiles.addAll(projectilesList);
        }
        for (Projectile projectile: projectiles) {
            projectile.move();
        }
    }

    private void spawnObstacles() {
        List<Vector2d> randomPositions = new ArrayList<Vector2d>(freePositions);
        Collections.shuffle(randomPositions);
        for (int i = 0; i < 1; i++) {
            if (randomPositions.size() - 1 < i) {
                return;
            }
            new Obstacle(randomPositions.get(i), this, observers);
        }
    }

    private void spawnEnemyTanks() {
        if (enemyTanks.size() > config.width()) {
            return;
        }
        List<Vector2d> randomPositions = new ArrayList<Vector2d>(freePositions);
        Collections.shuffle(randomPositions);
        for (int i = 0; i < 1; i++) {
            if (randomPositions.size() - 1 < i) {
                return;
            }
            new Tank(randomPositions.get(i), this, observers, false);
        }
    }

    private void moveEnemyTanks() {
        for (Tank enemy: enemyTanks) {
            enemy.makeMove();
        }
    }

    public void setPlayerTank(Tank playerTank) {
        this.playerTank = playerTank;
    }

    public void shootAtPlayer(Tank shooting) {

    }
}
