package expressions;

public class Number implements Expression {

    private final double value;

    public Number(final double value) {
        this.value = value;
    }

    @Override
    public double calculate() {
        return value;
    }
}
