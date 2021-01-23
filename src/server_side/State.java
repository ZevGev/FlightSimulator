package server_side;


public class State<T> {

    protected T state;
    private double cost;
    private State lastLocation;

    public State(T state) {
        this.state = state;
    }

    public State() {
        this.state = null;
        this.cost = 0;
        this.lastLocation = null;
    }

    public boolean equals(State state) {
        return this.state.equals(state.getState());
    }

    public T getState() {
        return state;
    }

    public void setState(T state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "State [state=" + state + ", cost=" + cost + ", from=" + lastLocation + "]";
    }

    public double getCost() {
        if (this.getLastLocation() != null) {
            return this.getLastLocation().getCost() + cost;
        } else {
            return cost;
        }
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public State getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(State lastLocation) {
        this.lastLocation = lastLocation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        return result;
    }


}
