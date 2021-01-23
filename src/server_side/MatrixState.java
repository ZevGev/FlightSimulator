package server_side;


public class MatrixState extends State<String> {

    public MatrixState(final String state) {
        super(state);
        this.setLastLocation(null);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        return result;
    }

    @Override
    public boolean equals(final State state) {
        return this.state.intern().equals(((String) state.getState()).intern());
    }
}
