package game;

import game.mapElements.IMapElement;
import game.mapElements.Tank;
import utils.Vector2d;

public interface IGameObserver {
    void handleTankRotate(Tank tank, int oldValue);
    void handleTankMoved(Tank element, Vector2d oldPosition);
    void handleTankAdded(Tank tank);
}
