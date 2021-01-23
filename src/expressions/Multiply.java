package expressions;

public class Multiply extends BinaryExpression {

    public Multiply(final Expression left, final Expression right) {
        super(left, right);
    }

    @Override
    public double calculate() {
        return left.calculate() * right.calculate();
    }

}
