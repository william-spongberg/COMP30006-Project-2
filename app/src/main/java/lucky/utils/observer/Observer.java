/**
 * Observer.java
 * contains an interface for defining what an observer should do on a state update
 *
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.observer;

import lucky.utils.state.StateData;
import lucky.utils.state.States;

public interface Observer {

    // when an observer sees that the state has been updated, it sees what the new state is, and executes the code of
    // instance(s) being observed for that state.
    public void onStateUpdate(States state, StateData stateData);

}
