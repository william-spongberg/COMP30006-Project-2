package game._observer;

import game._player.Player;
import game._state.StateData;
import game._state.States;

public interface Observer {

    public void onStateUpdate(States state, StateData stateData, Player[] newPlayers);

}
