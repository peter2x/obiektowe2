import config.Config;
import game.Game;
import visualization.GameVisualization;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Config config = new Config(10, 10);
        Game game = new Game(config);
        GameVisualization visualization = new GameVisualization(game, config);
        visualization.start(stage);
        game.start();
    }
}