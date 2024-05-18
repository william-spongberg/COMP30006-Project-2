package game._observer;

import game._state.State;

public interface Observer {

    public void onStateUpdate(State changedState);
}
