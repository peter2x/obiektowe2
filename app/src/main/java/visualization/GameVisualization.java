package visualization;

import config.Config;
import game.Game;
import game.IGameObserver;
import game.mapElements.IMapElement;
import game.mapElements.Tank;
import javafx.scene.input.KeyCode;
import utils.Vector2d;
import visualization.controllers.RootController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class GameVisualization {
    private static final String FXML_ROOT_PATH = "../fxml/root.fxml";
    private final Config config;
    private final Game game;
    private Scene scene;

    public GameVisualization(Game game, Config config) {
        this.config = config;
        this.game = game;
    }

    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT_PATH));
            Parent root = loader.load();
            RootController rootController = loader.getController();
            rootController.initialize(game, config);
            game.addObserver(rootController);
            scene = new Scene(root, 300, 275);
            attachKeyPressEvents();
            stage.setTitle("FXML Welcome");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void attachKeyPressEvents() {
        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case LEFT -> game.rotatePlayerTank(-1);
                case RIGHT -> game.rotatePlayerTank(1);
                case UP -> game.movePlayerTank(true);
                case DOWN -> game.movePlayerTank(false);
                case SPACE -> System.out.println("Space was pressed");
            }
        });
    }
}
