package game._state;


// this class is used to pass data for the different functions observers need. It is one of the params of notifyObservers
public class StateChange {

    private States state;
    private Object data;


    public StateChange(States state, Object data) {
        this.state = state;
        this.data = data;
    }

    public States getState() {
        return state;
    }

    public Object getData() {
        return data;
    }
}
