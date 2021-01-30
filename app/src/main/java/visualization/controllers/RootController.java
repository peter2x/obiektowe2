package visualization.controllers;

import config.Config;
import game.Game;
import game.IGameObserver;
import game.mapElements.IMapElement;
import game.mapElements.Projectile;
import game.mapElements.Tank;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import utils.Vector2d;
import visualization.controllers.field.FieldController;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RootController implements IGameObserver {
    private static final String FIELD_FXML_PATH = "../../fxml/field/field.fxml";
    @FXML private GridPane mapContainer;
    private Config config;
    private Game game;
    private FieldController[][] fieldControllers;
    private Map<Projectile, Vector2d> movedProjectilesCache = new HashMap<>();
    private Set<Vector2d> clearedFieldsCache = new HashSet<>();
    private Set<IMapElement> addedElementsCache = new HashSet<>();


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
    public void handleElementAdded(IMapElement element) {
        addedElementsCache.add(element);
    }

    @Override
    public void handleTankMoved(Tank moved, Vector2d oldPosition) {
        fieldControllers[oldPosition.x][oldPosition.y].removeElement();
        fieldControllers[moved.getPosition().x][moved.getPosition().y].addElement(moved);
        fieldControllers[moved.getPosition().x][moved.getPosition().y].removeProjectiles();
    }

    @Override
    public void handleProjectileMoved(Projectile moved, Vector2d oldPosition) {
        movedProjectilesCache.put(moved, oldPosition);
    }

    @Override
    public void handleTurnEnd() {
        moveProjectiles();
        clearFields();
        addElements();
    }

    @Override
    public void handleElementDestroyed(IMapElement element) {
        clearedFieldsCache.add(new Vector2d(element.getPosition().x, element.getPosition().y));
    }

    private void moveProjectiles() {
        for (Map.Entry<Projectile, Vector2d> entry: movedProjectilesCache.entrySet()) {
            Projectile moved = entry.getKey();
            Vector2d oldPosition = entry.getValue();
            fieldControllers[oldPosition.x][oldPosition.y].removeProjectile(moved);
            fieldControllers[moved.getPosition().x][moved.getPosition().y].addProjectile(moved);
        }
        movedProjectilesCache.clear();
    }

    @Override
    public void handleGameEnd(int finalScore) {
        mapContainer.getChildren().clear();
        mapContainer.add(new Text("final score is: " + finalScore), 0, 0);
    }

    private void clearFields() {
        for (Vector2d position: clearedFieldsCache) {
            fieldControllers[position.x][position.y].removeElement();
            fieldControllers[position.x][position.y].removeProjectiles();
        }
        clearedFieldsCache.clear();
    }

    private void addElements() {
        for (IMapElement element: addedElementsCache) {
            fieldControllers[element.getPosition().x][element.getPosition().y].addElement(element);
        }
        addedElementsCache.clear();
    }
}
