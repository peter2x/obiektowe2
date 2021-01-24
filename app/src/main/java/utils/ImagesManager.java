package utils;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;


public class ImagesManager {
    public static Map<String, Image> imagesCache = new HashMap<>();
    public static Image getImage(String url) {
        Image image = imagesCache.get(url);
        if (image == null) {
            image = new Image(url);
        }
        return image;
    }
}
