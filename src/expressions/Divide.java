package expressions;

public class Divide extends BinaryExpression {

    public Divide(final Expression left, final Expression right) {
        super(left, right);
    }

    @Override
    public double calculate() {
        return left.calculate() / right.calculate();
    }

}
