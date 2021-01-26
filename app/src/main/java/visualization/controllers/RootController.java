package visualization.controllers;

import config.Config;
import game.Game;
import game.IGameObserver;
import game.mapElements.Projectile;
import game.mapElements.Tank;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import utils.Vector2d;
import visualization.controllers.field.FieldController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RootController implements IGameObserver {
    private static final String FIELD_FXML_PATH = "../../fxml/field/field.fxml";
    @FXML private GridPane mapContainer;
    private Config config;
    private Game game;
    private FieldController[][] fieldControllers;
    private Map<Projectile, Vector2d> movedProjectilesCache = new HashMap<>();


    public void initialize(Game game, Config config) throws IOException {
        this.config = config;
        this.game = game;
        initializeChildren();
    }

    private void initializeChildren() throws IOException {
        fieldControllers = new FieldController[config.width()][config.height()];
        for (int i = 0; i < config.width(); i++) {
            for (int j = 0; j < config.height(); j++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(FIELD_FXML_PATH));
                StackPane fieldContainer = loader.load();
                mapContainer.add(fieldContainer, i, j);
                fieldControllers[i][config.height() - 1 - j] = loader.getController();
                fieldControllers[i][config.height() - 1 - j].initialize(game, config);
            }
        }
    }

    @Override
    public void handleTankRotate(Tank tank, int oldValue) {
        fieldControllers[tank.getPosition().x][tank.getPosition().y].setTankOrientation(tank.getOrientation());
    }

    @Override
    public void handleTankAdded(Tank tank) {
        fieldControllers[tank.getPosition().x][tank.getPosition().y].addTank(tank);
    }

    @Override
    public void handleTankMoved(Tank moved, Vector2d oldPosition) {
        fieldControllers[oldPosition.x][oldPosition.y].removeTank();
        fieldControllers[moved.getPosition().x][moved.getPosition().y].addTank(moved);
    }

    @Override
    public void handleProjectileMoved(Projectile moved, Vector2d oldPosition) {
        movedProjectilesCache.put(moved, oldPosition);
    }

    @Override
    public void handleTurnEnd() {
        moveProjectiles();
    }

    private void moveProjectiles() {
        for (Map.Entry<Projectile, Vector2d> entry: movedProjectilesCache.entrySet()) {
            Projectile moved = entry.getKey();
            Vector2d oldPosition = entry.getValue();
            fieldControllers[oldPosition.x][oldPosition.y].removeProjectile(moved);
            fieldControllers[moved.getPosition().x][moved.getPosition().y].addProjectile(moved);
        }
    }
}
