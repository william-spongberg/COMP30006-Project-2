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
    public void onStateUpdate(States state, StateData stateData);

}
