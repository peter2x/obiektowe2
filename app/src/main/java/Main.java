import config.Config;
import visualization.GameVisualization;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        System.out.println("hello world");
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Config config = new Config(15, 10);
        GameVisualization visualization = new GameVisualization(config);
        visualization.start(stage);
    }
}