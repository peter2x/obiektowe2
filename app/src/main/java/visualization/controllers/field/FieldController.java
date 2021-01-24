package visualization.controllers.field;
import config.Config;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import utils.ImagesManager;


public class FieldController {
    private static int index = 0;
    private static final String FIELD_TEXTURE_PATH = "earth.jpg";
    private static final String TANK_BLUE_TEXTURE_PATH = "tank_blue.png";
    private static final String TANK_RED_TEXTURE_PATH = "tank_red.png";
    private static final String PROJECTILE_TEXTURE_PATH = "projectile-removebg-preview.png";

    @FXML private ImageView field;
    @FXML private ImageView tank;
    @FXML private ImageView projectile;

    public void initialize(Config config) {
        index += 1;
        field.setImage(ImagesManager.getImage(FIELD_TEXTURE_PATH));
        field.setFitHeight((double)(900 / config.width()));
        field.setFitWidth((double)(900 / config.height()));
        tank.setImage(ImagesManager.getImage(index % 2 == 0 ? TANK_BLUE_TEXTURE_PATH : TANK_RED_TEXTURE_PATH));
        tank.setFitWidth((double)52);
        tank.setFitHeight((double)41);
        tank.rotateProperty().setValue(45 * index);
        projectile.setImage(ImagesManager.getImage(PROJECTILE_TEXTURE_PATH));
        projectile.setFitWidth((double)52);
        projectile.setFitHeight((double)41);
    }

    public void removeTank() {
        tank.setImage(null);
    }
}


