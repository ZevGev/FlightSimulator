package expressions;

public class Subtract extends BinaryExpression {

    public Subtract(final Expression left, final Expression right) {
        super(left, right);
    }

    @Override
    public double calculate() {
        return left.calculate() - right.calculate();
    }

}
