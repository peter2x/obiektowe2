package game;

import java.util.Collection;

public interface IGameEventPublisher {
    void addObserver(IGameObserver observer);
    void removeObserver(IGameObserver observer);
    void addAllObservers(Collection<? extends IGameObserver> observers);
}
