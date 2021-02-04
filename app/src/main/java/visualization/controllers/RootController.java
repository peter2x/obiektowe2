package visualization.controllers;

import config.Config;
import game.Game;
import game.IGameObserver;
import game.mapElements.IMapElement;
import game.mapElements.Projectile;
import game.mapElements.Tank;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import utils.Vector2d;
import visualization.controllers.field.FieldController;

import java.io.IOException;

public class RootController implements IGameObserver {
    private static final String FIELD_FXML_PATH = "../../fxml/field/field.fxml";
    @FXML private GridPane mapContainer;
    @FXML private Label scoreLabel;
    private Config config;
    private Game game;
    private FieldController[][] fieldControllers;
    private int score = 0;

    public void initialize(Game game, Config config) throws IOException {
        this.config = config;
        this.game = game;
        initializeChildren();
    }

    private void initializeChildren() throws IOException {
        fieldControllers = new FieldController[config.mapSize()][config.mapSize()];
        for (int i = 0; i < config.mapSize(); i++) {
            for (int j = 0; j < config.mapSize(); j++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(FIELD_FXML_PATH));
                StackPane fieldContainer = loader.load();
                mapContainer.add(fieldContainer, i, j);
                fieldControllers[i][config.mapSize() - 1 - j] = loader.getController();
                fieldControllers[i][config.mapSize() - 1 - j].initialize(game, config);
            }
        }
    }

    @Override
    public void handleTankRotate(Tank tank) {
        fieldControllers[tank.getPosition().x][tank.getPosition().y].setTankOrientation(tank.getOrientation());
    }

    @Override
    public void handleElementAdded(IMapElement element) {
        fieldControllers[element.getPosition().x][element.getPosition().y].addElement(element);
    }

    @Override
    public void handleTankMoved(Tank moved, Vector2d oldPosition) {
        fieldControllers[oldPosition.x][oldPosition.y].removeElement();
        fieldControllers[moved.getPosition().x][moved.getPosition().y].addElement(moved);
    }

    @Override
    public void handleProjectileMoved(Projectile moved, Vector2d oldPosition) {
        fieldControllers[oldPosition.x][oldPosition.y].removeProjectile(moved);
        fieldControllers[moved.getPosition().x][moved.getPosition().y].addProjectile(moved);
    }

    @Override
    public void handleTurnEnd() {

    }

    @Override
    public void handleElementDestroyed(IMapElement element) {
        fieldControllers[element.getPosition().x][element.getPosition().y].removeElement();
        fieldControllers[element.getPosition().x][element.getPosition().y].removeProjectiles();
        if (element instanceof Tank tank) {
            if (!tank.isPlayer()) {
                handleScoreIncrement();
            }
        }
    }


    @Override
    public void handleGameEnd(int finalScore) {
        mapContainer.getChildren().clear();
        mapContainer.add(new Text("You have lost"), 0, 0);
    }


    private void handleScoreIncrement() {
        score += 1;
        scoreLabel.setText("score: " + score);
    }
}
