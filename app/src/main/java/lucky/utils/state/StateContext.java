/**
 * StateContext.java
 * 
 * Represents the state context in the game. 
 * contains the StateContext class which is used to handle the state of the game. 
 * used to handle the state of the game.
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */


package lucky.utils.state;

import lucky.utils.observer.Observer;
import lucky.utils.player.Player;

import java.util.ArrayList;
import java.util.List;

public class StateContext {

    private List<Observer> observers = new ArrayList<>();

    private StateData stateData;

    States currentState;

    private Player[] players;

    public StateContext() {
        States currentState = States.START_GAME;
        StateData stateData = new StateData();
    }

    public void addObservers(Observer observer) {
        observers.add(observer);
    }

    public void setCurrentState(States currentState, StateData stateData) {
        this.currentState = currentState;
        this.stateData = stateData;
        notifyObservers();
    }

    // call this to run the onStateupdate of all observers.
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.onStateUpdate(currentState, stateData);
        }
    }
}
