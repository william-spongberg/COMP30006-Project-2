package game._state;

import game._observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class StateContext {

    private States currentState;
    private List<Observer> observers = new ArrayList<>();

    public StateContext() {
        currentState = States.START_GAME;
    }

    public void addObservers(Observer observer) {
        observers.add(observer);
    }
    public States getCurrentState() {
        return currentState;
    }
    public void setCurrentState(States currentState) {
        this.currentState = currentState;
        notifyObservers();
    }

    // call this to run the onStateupdate of all observers.
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.onStateUpdate(currentState);
        }
    }

    // TODO: PROBABLY REMOVE THIS?
    // call this when changing state to run the code of each state
    // public void stateChanging() {
    //    currentState.StateHandle(this);
    //}
}
