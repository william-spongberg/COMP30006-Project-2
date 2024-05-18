package game._state;

import game._observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class StateContext {

    //TODO HANDLE STATE ON STATE CHANGE

    private State currentState;
    private List<Observer> observers = new ArrayList<>();

    public StateContext() {
        currentState = new GameStart();
    }

    public void addObservers(Observer observer) {
        observers.add(observer);
    }


    public State getCurrentState() {
        return currentState;
    }
    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    // call this to run the onStateupdate of all observers.
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.onStateUpdate(currentState);
        }
    }
}
