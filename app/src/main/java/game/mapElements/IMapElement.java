package game.mapElements;


import utils.Vector2d;

public interface IMapElement {
    Vector2d getPosition();
    boolean isBlocking();
    void getShoot();
}
