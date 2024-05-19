package game._state;

import game._observer.Observer;
import game._player.Player;

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
    public States getCurrentState() {
        return currentState;
    }
    public void setCurrentState(States currentState, StateData stateData) {
        this.currentState = currentState;
        this.stateData = stateData;
        notifyObservers();
    }

    // call this when we update state and we need players.
    // TODO. This sucks.
     public void updatePlayers(Player[] newPlayers) {
        this.players = newPlayers;
     }

    // call this to run the onStateupdate of all observers.
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.onStateUpdate(currentState, stateData, players);
        }
    }

    // TODO: PROBABLY REMOVE THIS?
    // call this when changing state to run the code of each state
    // public void stateChanging() {
    //    currentState.StateHandle(this);
    //}
}
