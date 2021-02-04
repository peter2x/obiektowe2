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
    private IMapElement shotElement = null;
    private int lastSpawnedTankTimeGap = 0;
    private int lastSpawnedObstacleTimeGap = 0;

    public WorldMap(Game game, Config config, List<IGameObserver> observers) {
        this.config = config;
        this.observers = observers;
        this.game = game;
        for (int i = 0; i < config.mapSize(); i++) {
            for (int j = 0; j < config.mapSize(); j++) {
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
        Vector2d upperRight =  new Vector2d(config.mapSize() - 1, config.mapSize() - 1);
        return position.fitToRectangle(lowerLeft, upperRight);
    }


    @Override
    public void handleTankRotate(Tank tank) {

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
            shotElement = moved;
        }
        if (!moved.isPlayer() && !positionToProjectiles.get(moved.getPosition()).isEmpty()) {
            positionToProjectiles.get(moved.getPosition()).clear();
            shotElement = moved;
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
            shotElement = blockingObjectAt(newPosition);
        } else {
            positionToProjectiles.putIfAbsent(moved.getPosition(), new LinkedList<>());
            positionToProjectiles.get(moved.getPosition()).add(moved);
            freePositions.remove(moved.getPosition());
        }
    }

    @Override
    public void handleTurnEnd() {
        checkShotElement();
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
            checkShotElement();
        }
    }

    private void spawnObstacles() {
        Boolean shouldSpawnObstacle = (int)(Math.random() * config.maxObstacleSpawnTimeGap() / 3) == 0;
        if (lastSpawnedObstacleTimeGap == config.maxObstacleSpawnTimeGap()) {
            shouldSpawnObstacle = true;
        }
        if (!shouldSpawnObstacle) {
            lastSpawnedObstacleTimeGap++;
            return;
        }
        List<Vector2d> randomPositions = new ArrayList<Vector2d>(freePositions);
        Collections.shuffle(randomPositions);
        if (randomPositions.size() == 0) {
            return;
        }
        new Obstacle(randomPositions.get(0), this, observers);
        lastSpawnedObstacleTimeGap = 1;
    }

    private void spawnEnemyTanks() {
        Boolean shouldSpawnTank = (int)(Math.random() * config.maxTankSpawnTimeGap() / 3) == 0;
        if (enemyTanks.size() == 0 || lastSpawnedTankTimeGap == config.maxTankSpawnTimeGap()) {
            shouldSpawnTank = true;
        }
        if (!shouldSpawnTank) {
            lastSpawnedTankTimeGap++;
            return;
        }
        List<Vector2d> randomPositions = new ArrayList<Vector2d>(freePositions);
        Collections.shuffle(randomPositions);
        if (randomPositions.size() == 0) {
            return;
        }
        new Tank(randomPositions.get(0), this, observers, false);
        lastSpawnedTankTimeGap = 1;
    }

    private void moveEnemyTanks() {
        List<Tank> tanksToMove = new LinkedList<>(enemyTanks);
        for (Tank enemy: tanksToMove) {
            enemy.makeMove();
            checkShotElement();
        }
    }

    public void setPlayerTank(Tank playerTank) {
        this.playerTank = playerTank;
    }

    public Vector2d getPlayerTankPosition() {
        return playerTank.getPosition();
    }

    public void shootAtPlayer(Tank shooting) {
        positionToProjectiles.putIfAbsent(shooting.getPosition(), new LinkedList<>());
        Projectile created = new Projectile(shooting.getOrientation(), shooting.getPosition(), this, observers);
        positionToProjectiles.get(shooting.getPosition()).add(created);
    }

    private void checkShotElement() {
        if (shotElement != null) {
            shotElement.getShoot();
            shotElement = null;
        }
    }
}
