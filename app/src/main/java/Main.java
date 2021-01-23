import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        System.out.println("hello world");
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.show();
    }
}