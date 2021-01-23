package expressions;

public class Or extends BinaryExpression {
    public Or(final Expression left, final Expression right) {
        super(left, right);
    }

    @Override
    public double calculate() {
        if ((left.calculate() + right.calculate()) > 0) {
            return 1;
        }
        return 0;
    }
}
