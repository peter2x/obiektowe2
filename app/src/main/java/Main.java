import config.Config;
import config.ConfigParser;
import game.Game;
import org.json.simple.parser.ParseException;
import visualization.GameVisualization;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            Config config = ConfigParser.parse("parameters.json");
            Game game = new Game(config);
            GameVisualization visualization = new GameVisualization(game, config);
            visualization.start(stage);
            game.start();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}