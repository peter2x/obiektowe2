package game;

import game.mapElements.IMapElement;
import game.mapElements.Tank;
import utils.Vector2d;

public interface IGameObserver {
    void handleTankRotate(Tank tank, int oldValue);
    void handleElementPositionChange(IMapElement element, Vector2d oldPosition);
}
