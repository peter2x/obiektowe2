package visualization.controllers;

import config.Config;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import visualization.controllers.field.FieldController;

import java.io.IOException;

public class RootController {
    private static final String FIELD_FXML_PATH = "../../fxml/field/field.fxml";
    @FXML private GridPane mapContainer;
    private Config config;
    private FieldController[][] fieldControllers;


    public void initialize(Config config) throws IOException {
        this.config = config;
        initializeChildren();
    }

    private void initializeChildren() throws IOException {
        fieldControllers = new FieldController[config.width()][config.height()];
        for (int i = 0; i < config.width(); i++) {
            for (int j = 0; j < config.height(); j++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(FIELD_FXML_PATH));
                StackPane fieldContainer = loader.load();
                mapContainer.add(fieldContainer, i, j);
                fieldControllers[i][j] = loader.getController();
                fieldControllers[i][j].initialize(config);
            }
        }
    }
}
