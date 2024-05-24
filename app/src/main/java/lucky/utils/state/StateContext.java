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

import java.util.ArrayList;
import java.util.List;

public class StateContext {
    // attributes
    private final List<Observer> observers = new ArrayList<>();
    private StateData stateData;
    private States currentState;

    public StateContext() {
        this.currentState = States.START_GAME;
        this.stateData = new StateData();
    }

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer the observer to be added
     */
    public void addObservers(Observer observer) {
        observers.add(observer);
    }

    /**
     * Sets the current state of the StateContext and updates the state data.
     * Notifies the observers after updating the state.
     *
     * @param currentState the new current state to be set
     * @param stateData the new state data to be associated with the current state
     */
    public void setCurrentState(States currentState, StateData stateData) {
        this.currentState = currentState;
        this.stateData = stateData;
        notifyObservers();
    }

    /**
     * Notifies all the registered observers about the state update.
     * This method calls the `onStateUpdate` method of each observer,
     * passing the current state and state data as parameters.
     */
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.onStateUpdate(currentState, stateData);
        }
    }
}
