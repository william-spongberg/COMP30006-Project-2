package game._observer;

import game._state.States;

public interface Observer {

    public void onStateUpdate(States changedState);
}
