/**
 * State.java
 * 
 * Represents the state of the game.
 * contains an interface State which is implemented by the StateContext class.
 * used to handle the state of the game.
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.state;

public interface State {

    public void StateHandle(StateContext context);

}
