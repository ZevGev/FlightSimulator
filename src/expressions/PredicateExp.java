package expressions;

import java.util.function.Predicate;


public class PredicateExp extends BinaryExpression {
    final String condition;
    Predicate<Expression> predicate;

    public PredicateExp(final Expression left, final Expression right, final String condition) {
        super(left, right);
        this.condition = condition;
    }

    @Override
    public double calculate() {
        switch (condition) {
            case "<":
                predicate = expression -> expression.calculate() < right.calculate();
                break;
            case ">":
                predicate = expression -> expression.calculate() > right.calculate();
                break;
            case "<=":
                predicate = expression -> expression.calculate() <= right.calculate();
                break;
            case ">=":
                predicate = expression -> expression.calculate() >= right.calculate();
                break;
            case "==":
                predicate = expression -> expression.calculate() == right.calculate();
                break;
            case "!=":
                predicate = expression -> expression.calculate() != right.calculate();
                break;
            default:
                break;
        }

        if (predicate.test(left)) {
            return 1;
        }
        return 0;
    }

}
