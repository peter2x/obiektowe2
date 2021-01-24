package visualization;

import config.Config;
import javafx.scene.input.KeyCode;
import visualization.controllers.RootController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class GameVisualization {
    private static final String FXML_ROOT_PATH = "../fxml/root.fxml";
    private final Config config;
    private Scene scene;

    public GameVisualization(Config config) {
        this.config = config;
    }

    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT_PATH));
            Parent root = loader.load();
            RootController rootController = loader.getController();
            rootController.initialize(config);
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
            if (e.getCode() == KeyCode.LEFT) {
                System.out.println("A left arrow was pressed");
            }
            if (e.getCode() == KeyCode.RIGHT) {
                System.out.println("A right arrow was pressed");
            }
        });
    }
}
