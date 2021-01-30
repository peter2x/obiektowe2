package visualization.controllers.field;
import config.Config;
import game.Game;
import game.mapElements.IMapElement;
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
    private static final String OBSTACLE_TEXTURE_PATH = "obstacle.jpg";

    @FXML private ImageView field;
    @FXML private ImageView element;
    @FXML private StackPane fieldContainer;
    private Game game;
    private Config config;
    private final Map<Projectile, ImageView> projectileToImages = new HashMap<>();

    public void initialize(Game game, Config config) {
        field.setImage(ImagesManager.getImage(FIELD_TEXTURE_PATH));
        field.setFitHeight((double)(900 / config.width()));
        field.setFitWidth((double)(900 / config.height()));
        this.config = config;
    }

    public void setTankOrientation(Orientation orientation) {
        element.rotateProperty().setValue(45 * orientation.getValue());
    }

    public void addElement(IMapElement added) {
        if (added instanceof Tank tank) {
            element.setImage(ImagesManager.getImage(tank.isPlayer() ? TANK_BLUE_TEXTURE_PATH : TANK_RED_TEXTURE_PATH));
            element.rotateProperty().setValue(45 * tank.getOrientation().getValue());
            element.setFitWidth((double)52);
            element.setFitHeight((double)41);
        } else {
            element.setImage(ImagesManager.getImage(OBSTACLE_TEXTURE_PATH));
            element.setFitHeight((double)(900 / config.width()));
            element.setFitWidth((double)(900 / config.height()));
        }
    }

    public void removeProjectiles() {
        fieldContainer.getChildren().removeAll(projectileToImages.values());
        projectileToImages.clear();
    }

    public void removeProjectile(Projectile removedProjectile) {
        ImageView projectileImage = projectileToImages.get(removedProjectile);
        projectileToImages.remove(removedProjectile);
        fieldContainer.getChildren().remove(projectileImage);
    }

    public void addProjectile(Projectile addedProjectile) {
        if (element.getImage() != null) {
            return;
        }
        ImageView projectileImage = new ImageView(ImagesManager.getImage(PROJECTILE_TEXTURE_PATH));
        projectileImage.setFitWidth((double)52);
        projectileImage.setFitHeight((double)41);
        projectileImage.rotateProperty().setValue(addedProjectile.getOrientation().getValue() * 45);
        projectileToImages.put(addedProjectile, projectileImage);
        fieldContainer.getChildren().add(projectileImage);
    }

    public void removeElement() {
        element.setImage(null);
    }

}


