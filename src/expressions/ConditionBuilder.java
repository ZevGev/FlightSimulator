package expressions;

import java.util.LinkedList;
import java.util.Stack;

public class ConditionBuilder {

    public static double calc(String expression) {
        final LinkedList<String> queue = new LinkedList<>();
        final Stack<String> stack = new Stack<>();
        String token = "";

        while (expression.contains("&") || expression.contains("|")) {
            int i = expression.indexOf("&");
            int j = expression.indexOf("|");

            if ((j < i && j != -1) || i == -1) {
                i = j;
            }

            token = ShuntingYardPredicate.calc(expression.substring(0, i)) + "";
            queue.addFirst(token);
            token = expression.charAt(i) + "";

            switch (token) {
                case "|":
                    while (!stack.isEmpty()) {
                        queue.addFirst(stack.pop());
                    }
                    stack.push(token);
                    break;
                case "&":
                    while (!stack.isEmpty() && (stack.peek().equals("&"))) {
                        queue.addFirst(stack.pop());
                    }
                    stack.push(token);
                    break;
                default:
                    queue.addFirst(token);
                    break;
            }
            expression = expression.substring(i + 2);
        }
        token = ShuntingYardPredicate.calc(expression) + "";
        queue.addFirst(token);
        while (!stack.isEmpty()) {
            queue.addFirst(stack.pop());
        }
        final Expression finalExpression = buildExpression(queue);
        final double answer = finalExpression.calculate();
        return Double.parseDouble(String.format("%.3f", answer));
    }

    private static Expression buildExpression(final LinkedList<String> queue) {
        Expression returnedExpression;
        Expression right = null;
        Expression left = null;
        String currentExpression = queue.removeFirst();

        if (currentExpression.equals("|") || currentExpression.equals("&")) {
            right = buildExpression(queue);
            left = buildExpression(queue);
        }

        switch (currentExpression) {
            case "|":
                returnedExpression = new Or(left, right);
                break;
            case "&":
                returnedExpression = new And(left, right);
                break;
            default:
                returnedExpression = new Number(Double.parseDouble(String.format("%.2f", Double.parseDouble(currentExpression))));
                break;
        }
        return returnedExpression;
    }
}
