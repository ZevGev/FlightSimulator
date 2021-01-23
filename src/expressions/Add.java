package expressions;

public class Add extends BinaryExpression {

    public Add(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double calculate() {
        return left.calculate() + right.calculate();
    }

}
