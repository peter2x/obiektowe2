package visualization.controllers.field;
import config.Config;
import game.Game;
import game.mapElements.Orientation;
import game.mapElements.Projectile;
import game.mapElements.Tank;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import utils.ImagesManager;

import java.util.HashMap;
import java.util.Map;


public class FieldController {
    private static final String FIELD_TEXTURE_PATH = "earth.jpg";
    private static final String TANK_BLUE_TEXTURE_PATH = "tank_blue.png";
    private static final String TANK_RED_TEXTURE_PATH = "tank_red.png";
    private static final String PROJECTILE_TEXTURE_PATH = "projectile-removebg-preview.png";

    @FXML private ImageView field;
    @FXML private ImageView tank;
    @FXML private StackPane fieldContainer;
    private Game game;
    private final Map<Projectile, ImageView> projectileToImages = new HashMap<>();

    public void initialize(Game game, Config config) {
        field.setImage(ImagesManager.getImage(FIELD_TEXTURE_PATH));
        field.setFitHeight((double)(900 / config.width()));
        field.setFitWidth((double)(900 / config.height()));
        // projectile.setImage(ImagesManager.getImage(PROJECTILE_TEXTURE_PATH));
        // projectile.setFitWidth((double)52);
        // projectile.setFitHeight((double)41);
    }

    public void setTankOrientation(Orientation orientation) {
        tank.rotateProperty().setValue(45 * orientation.getValue());
    }

    public void addTank(Tank added) {
        tank.setImage(ImagesManager.getImage(added.isPlayer() ? TANK_BLUE_TEXTURE_PATH : TANK_RED_TEXTURE_PATH));
        tank.rotateProperty().setValue(45 * added.getOrientation().getValue());
        tank.setFitWidth((double)52);
        tank.setFitHeight((double)41);
    }

    public void removeProjectile(Projectile removedProjectile) {
        ImageView projectileImage = projectileToImages.get(removedProjectile);
        projectileToImages.remove(removedProjectile);
        fieldContainer.getChildren().remove(projectileImage);
    }

    public void addProjectile(Projectile addedProjectile) {
        ImageView projectileImage = new ImageView(ImagesManager.getImage(PROJECTILE_TEXTURE_PATH));
        projectileImage.setFitWidth((double)52);
        projectileImage.setFitHeight((double)41);
        projectileImage.rotateProperty().setValue(addedProjectile.getOrientation().getValue() * 45);
        projectileToImages.put(addedProjectile, projectileImage);
        fieldContainer.getChildren().add(projectileImage);
    }

    public void removeTank() {
        tank.setImage(null);
    }
}


